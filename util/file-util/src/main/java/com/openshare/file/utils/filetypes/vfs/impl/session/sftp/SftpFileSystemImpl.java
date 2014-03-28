package com.openshare.file.utils.filetypes.vfs.impl.session.sftp;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.GenericFileName;
import org.apache.commons.vfs2.provider.sftp.SftpFileProvider;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystem;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;
/**
 * extension class so we can customise the SftpClientFactory and FileProviders.
 * WARNING: THIS FILE IS A DIRECT COPY WITH MODIFICATIONS OF SftpFileSystem AS
 * THE MEMBER VARIABLES WITHIN ARE PRIVATE AND THUS WE HAVE A PROBLEM OVERRIDING 
 * FUNCTIONALITY SAVE HACKING AND MODIFYING THE UNDERLYING LIBRARY
 * @author james.mcilroy
 *
 */
public class SftpFileSystemImpl extends SftpFileSystem {

	/**
	 * constructor used to access the SftpFileSystem as it 
	 * is package protected in VFS
	 * @param rootName
	 * @param session
	 * @param fileSystemOptions
	 */
	protected SftpFileSystemImpl(GenericFileName rootName, Session session,
			FileSystemOptions fileSystemOptions) {
		super(rootName, session, fileSystemOptions);
	}

	protected Session session;
    // private final JSch jSch;
    protected ChannelSftp idleChannel;

    @Override
    protected void doCloseCommunicationLink()
    {
        if (idleChannel != null)
        {
            idleChannel.disconnect();
            idleChannel = null;
        }

        if (session != null)
        {
            session.disconnect();
            session = null;
        }
    }

    /**
     * Returns an SFTP channel to the server.
     */
    protected ChannelSftp getChannel() throws IOException
    {
        if (this.session == null || !this.session.isConnected())
        {
            doCloseCommunicationLink();

            // channel closed. e.g. by freeUnusedResources, but now we need it again
            Session session;
            UserAuthenticationData authData = null;
            String fingerPrint=null;
            String passPhrase=null;
            DiskFile privateKey = null;
            try
            {
                final GenericFileName rootName = (GenericFileName) getRootName();
                //we are going to need to extract the user auth data we are currently using.
                UserAuthenticator auth = DefaultFileSystemConfigBuilder.getInstance().getUserAuthenticator(getFileSystemOptions());
                //get the user authenticator, see if it's one of ours, if so, extract required fingerprint/passphrase/key
                //Remember that anyting changed here SHOULD mirror changes in SftpFileProviderImpl for ssh as otherwise 
                //some operations on the server WILL differ... This is a VFS architectural limitation.
                if(auth instanceof UserAuthenticatorImpl){
                	fingerPrint = ((UserAuthenticatorImpl)auth).getKeyFingerprint();
                	privateKey = ((UserAuthenticatorImpl)auth).getPrivateKey();
                	passPhrase = ((UserAuthenticatorImpl)auth).getPassPhrase();
                }
                
                authData = UserAuthenticatorUtils.authenticate(getFileSystemOptions(),
                    SftpFileProvider.AUTHENTICATOR_TYPES);
              
                session = SftpClientFactoryImpl.createConnection(
                    rootName.getHostName(),
                    rootName.getPort(),
                    UserAuthenticatorUtils.getData(authData, UserAuthenticationData.USERNAME,
                        UserAuthenticatorUtils.toChar(rootName.getUserName())),
                    UserAuthenticatorUtils.getData(authData, UserAuthenticationData.PASSWORD,
                        UserAuthenticatorUtils.toChar(rootName.getPassword())),
                        fingerPrint,
                        passPhrase,
                        privateKey,
                    getFileSystemOptions());
            }
            catch (final Exception e)
            {
                throw new FileSystemException("vfs.provider.sftp/connect.error",
                    getRootName(),
                    e);
            }
            finally
            {
                UserAuthenticatorUtils.cleanup(authData);
            }

            this.session = session;
        }

        try
        {
            // Use the pooled channel, or create a new one
            final ChannelSftp channel;
            if (idleChannel != null)
            {
                channel = idleChannel;
                idleChannel = null;
            }
            else
            {
                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();

                Boolean userDirIsRoot =
                    SftpFileSystemConfigBuilder.getInstance().getUserDirIsRoot(getFileSystemOptions());
                String workingDirectory = getRootName().getPath();
                if (workingDirectory != null && (userDirIsRoot == null || !userDirIsRoot.booleanValue()))
                {
                    try
                    {
                        channel.cd(workingDirectory);
                    }
                    catch (SftpException e)
                    {
                        throw new FileSystemException("vfs.provider.sftp/change-work-directory.error",
                            workingDirectory);
                    }
                }
            }

            return channel;
        }
        catch (final JSchException e)
        {
            throw new FileSystemException("vfs.provider.sftp/connect.error",
                getRootName(),
                e);
        }
    }

    /**
     * Returns a channel to the pool.
     */
    protected void putChannel(final ChannelSftp channel)
    {
        if (idleChannel == null)
        {
            // put back the channel only if it is still connected
            if (channel.isConnected() && !channel.isClosed())
            {
                idleChannel = channel;
            }
        }
        else
        {
            channel.disconnect();
        }
    }

    /**
     * Adds the capabilities of this file system.
     */
    @Override
    protected void addCapabilities(final Collection<Capability> caps)
    {
        caps.addAll(SftpFileProviderImpl.capabilities);
    }
}
