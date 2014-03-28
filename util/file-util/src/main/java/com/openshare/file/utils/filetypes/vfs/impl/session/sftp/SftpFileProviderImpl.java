package com.openshare.file.utils.filetypes.vfs.impl.session.sftp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.GenericFileName;
import org.apache.commons.vfs2.provider.sftp.SftpFileNameParser;
import org.apache.commons.vfs2.provider.sftp.SftpFileProvider;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import com.jcraft.jsch.Session;
import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;

public class SftpFileProviderImpl extends SftpFileProvider{
    /** User Information. */
    public static final String ATTR_USER_INFO = "UI";

    /** Authentication types. */
    public static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES =
        new UserAuthenticationData.Type[]
            {
                UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD
            };

    /** The provider's capabilities. */
    protected static final Collection<Capability> capabilities =
        Collections.unmodifiableCollection(Arrays.asList(new Capability[]
    {
        Capability.CREATE,
        Capability.DELETE,
        Capability.RENAME,
        Capability.GET_TYPE,
        Capability.LIST_CHILDREN,
        Capability.READ_CONTENT,
        Capability.URI,
        Capability.WRITE_CONTENT,
        Capability.GET_LAST_MODIFIED,
        Capability.SET_LAST_MODIFIED_FILE,
        Capability.RANDOM_ACCESS_READ
    }));

    // private JSch jSch = new JSch();

    public SftpFileProviderImpl()
    {
        super();
        setFileNameParser(SftpFileNameParser.getInstance());
    }

    /**
     * Creates a {@link FileSystem}.
     */
    @Override
    protected FileSystem doCreateFileSystem(final FileName name, final FileSystemOptions fileSystemOptions)
        throws FileSystemException
    {
        // JSch jsch = createJSch(fileSystemOptions);

        // Create the file system
        final GenericFileName rootName = (GenericFileName) name;

        Session session;
        UserAuthenticationData authData = null;
        String fingerprint=null;
        String passPhrase=null;
        DiskFile privateKey = null;
        try
        {
            authData = UserAuthenticatorUtils.authenticate(fileSystemOptions, AUTHENTICATOR_TYPES);
            //get the user authenticator, see if it's one of ours, if so, extract required fingerprint/passphrase/key
            //Remember that anyting changed here SHOULD mirror changes in SftpFileSystemImpl for ssh as otherwise 
            //some operations on the server WILL differ... This is a VFS architectural limitation.
            UserAuthenticator auth = DefaultFileSystemConfigBuilder.getInstance().getUserAuthenticator(fileSystemOptions);
            if(auth instanceof UserAuthenticatorImpl){
            	fingerprint = ((UserAuthenticatorImpl)auth).getKeyFingerprint();
            	privateKey = ((UserAuthenticatorImpl)auth).getPrivateKey();
            	passPhrase = ((UserAuthenticatorImpl)auth).getPassPhrase();
            }
            session = SftpClientFactoryImpl.createConnection(
                rootName.getHostName(),
                rootName.getPort(),
                UserAuthenticatorUtils.getData(authData, UserAuthenticationData.USERNAME,
                    UserAuthenticatorUtils.toChar(rootName.getUserName())),
                UserAuthenticatorUtils.getData(authData, UserAuthenticationData.PASSWORD,
                    UserAuthenticatorUtils.toChar(rootName.getPassword())),
                    fingerprint,
                    passPhrase,
                    privateKey,
                fileSystemOptions);
        }
        catch (final Exception e)
        {
            throw new FileSystemException("vfs.provider.sftp/connect.error",
                name,
                e);
        }
        finally
        {
            UserAuthenticatorUtils.cleanup(authData);
        }

        return new SftpFileSystemImpl(rootName, session, fileSystemOptions);
    }
}
