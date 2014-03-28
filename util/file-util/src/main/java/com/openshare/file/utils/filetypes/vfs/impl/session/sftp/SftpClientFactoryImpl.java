package com.openshare.file.utils.filetypes.vfs.impl.session.sftp;

import java.io.File;
import java.util.Properties;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.commons.vfs2.util.Os;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;
/**
 * Override of SftpClientFactory so we can use key fingerprints and 
 * other good stuff.
 * @author james.mcilroy
 *
 */
public class SftpClientFactoryImpl {

    private static final String SSH_DIR_NAME = ".ssh";

    private SftpClientFactoryImpl()
    {
    }

    /**
     * Creates a new connection to the server.
     * @param hostname The name of the host to connect to.
     * @param port The port to use.
     * @param username The user's id.
     * @param password The user's password.
     * @param fileSystemOptions The FileSystem options.
     * @return A Session.
     * @throws FileSystemException if an error occurs.
     */
    public static Session createConnection(String hostname, int port, char[] username, char[] password, 
    									   String fingerPrint,String passPhrase,DiskFile privateKey, 
    									   FileSystemOptions fileSystemOptions) throws FileSystemException
    {
        JSch jsch = new JSch();

        File sshDir = null;

        // new style - user passed
        File knownHostsFile = SftpFileSystemConfigBuilder.getInstance().getKnownHosts(fileSystemOptions);
        File[] identities = SftpFileSystemConfigBuilder.getInstance().getIdentities(fileSystemOptions);

        if (knownHostsFile != null)
        {
            try
            {
                jsch.setKnownHosts(knownHostsFile.getAbsolutePath());
            }
            catch (JSchException e)
            {
                throw new FileSystemException("vfs.provider.sftp/known-hosts.error",
                    knownHostsFile.getAbsolutePath(), e);
            }
        }
        else
        {
            sshDir = findSshDir();
            // Load the known hosts file
            knownHostsFile = new File(sshDir, "known_hosts");
            if (knownHostsFile.isFile() && knownHostsFile.canRead())
            {
                try
                {
                    jsch.setKnownHosts(knownHostsFile.getAbsolutePath());
                }
                catch (JSchException e)
                {
                    throw new FileSystemException("vfs.provider.sftp/known-hosts.error",
                        knownHostsFile.getAbsolutePath(), e);
                }
            }
        }

        if (identities != null)
        {
            for (int iterIdentities = 0; iterIdentities < identities.length; iterIdentities++)
            {
                final File privateKeyFile = identities[iterIdentities];
                try
                {
		            if (passPhrase != null && !passPhrase.isEmpty()){
		                jsch.addIdentity(privateKey.getFullPathName(),passPhrase);
		            }else {
		                jsch.addIdentity(privateKey.getFullPathName());                    
		            }
                }
                catch (final JSchException e)
                {
                    throw new FileSystemException("vfs.provider.sftp/load-private-key.error", privateKeyFile, e);
                }
            }
        }
        else
        {
            if (sshDir == null)
            {
                sshDir = findSshDir();
            }
            //if a private key has been provided then load that, otherwise look for one in the 
            //specified SSH directory with a default name of "id_rsa"
            try{
            	if(privateKey!=null && privateKey.exists() && !privateKey.isFolder()){
            		try
	                {
            			if (passPhrase != null && !passPhrase.isEmpty()){
    		                jsch.addIdentity(privateKey.getFullPathName(),passPhrase);
    		            }else {
    		                jsch.addIdentity(privateKey.getFullPathName());                    
    		            }
	                }
	                catch (final JSchException jse)
	                {
	                    throw new FileSystemException("vfs.provider.sftp/load-private-key.error", privateKey.getFullPathName(), jse);
	                }
            	}
            	else{
            		//throw an exception so we can try with the below code 
            		//which we would normally execute
            	}
            }
            catch(Exception e){
            	//fall back on old implementation
	            // Load the private key (rsa-key only)
	            //final File privateKeyFile = new File(sshDir, "id_rsa");
	            /*
	            if (privateKeyFile.isFile() && privateKeyFile.canRead())
	            {
	                try
	                {
	                    jsch.addIdentity(privateKeyFile.getAbsolutePath());
	                }
	                catch (final JSchException jse)
	                {
	                */
	                    throw new FileSystemException("vfs.provider.sftp/load-private-key.error", e);
	                /*}
	            }
        		*/
            }
        }

        Session session;
        try
        {
            session = jsch.getSession(new String(username),
                    hostname,
                    port);
            if (password != null)
            {
                session.setPassword(new String(password));
            }

            Integer timeout = SftpFileSystemConfigBuilder.getInstance().getTimeout(fileSystemOptions);
            if (timeout != null)
            {
                session.setTimeout(timeout.intValue());
            }

            UserInfo userInfo = SftpFileSystemConfigBuilder.getInstance().getUserInfo(fileSystemOptions);
            if (userInfo != null)
            {
                session.setUserInfo(userInfo);
            }

            Properties config = new Properties();

            //set StrictHostKeyChecking property
            String strictHostKeyChecking =
                SftpFileSystemConfigBuilder.getInstance().getStrictHostKeyChecking(fileSystemOptions);
            if (strictHostKeyChecking != null)
            {
                config.setProperty("StrictHostKeyChecking", strictHostKeyChecking);
            }
            //set PreferredAuthentications property
            String preferredAuthentications = SftpFileSystemConfigBuilder.getInstance().
            getPreferredAuthentications(fileSystemOptions);
            if (preferredAuthentications != null)
            {
                config.setProperty("PreferredAuthentications", preferredAuthentications);
            }

            //set compression property
            String compression = SftpFileSystemConfigBuilder.getInstance().getCompression(fileSystemOptions);
            if (compression != null)
            {
                config.setProperty("compression.s2c", compression);
                config.setProperty("compression.c2s", compression);
            }

            String proxyHost = SftpFileSystemConfigBuilder.getInstance().getProxyHost(fileSystemOptions);
            if (proxyHost != null)
            {
                int proxyPort = SftpFileSystemConfigBuilder.getInstance().getProxyPort(fileSystemOptions);
                SftpFileSystemConfigBuilder.ProxyType proxyType =
                    SftpFileSystemConfigBuilder.getInstance().getProxyType(fileSystemOptions);
                Proxy proxy = null;
                if (SftpFileSystemConfigBuilder.PROXY_HTTP.equals(proxyType))
                {
                    if (proxyPort != 0)
                    {
                        proxy = new ProxyHTTP(proxyHost, proxyPort);
                    }
                    else
                    {
                        proxy = new ProxyHTTP(proxyHost);
                    }
                }
                else if (SftpFileSystemConfigBuilder.PROXY_SOCKS5.equals(proxyType))
                {
                    if (proxyPort != 0)
                    {
                        proxy = new ProxySOCKS5(proxyHost, proxyPort);
                    }
                    else
                    {
                        proxy = new ProxySOCKS5(proxyHost);
                    }
                }

                if (proxy != null)
                {
                    session.setProxy(proxy);
                }
            }
            //check host key
            //HostKey key = session.getHostKey();
            HostKeyRepository hostRepo = jsch.getHostKeyRepository();
            if(hostRepo!=null){
	            //ghet all the keys we have stored for this host
	            HostKey keys [] = hostRepo.getHostKey(hostname, "ssh-rsa");
	            //check the keys to make sure one conforms to the supplied fingerprint
	            boolean hostKeySignatureFailure = true;
	            if(keys!=null){
		            for(HostKey key : keys){
		            	String hostKeyFingerPrint = key.getFingerPrint(jsch);
		            	//check
		            	if(hostKeyFingerPrint.equals(fingerPrint)){
		            		//if at least one matches, set to false
		            		hostKeySignatureFailure = false;
		            		break;
		            	}
		            }
		            if(hostKeySignatureFailure){
		        		throw new FileSystemException("Failed to match required fingerprint when looking through hosts");
		        	}
	            }
	        }
            //set properties for the session
            if (config.size() > 0)
            {
                session.setConfig(config);
            }
            session.setDaemonThread(true);
            session.connect();
        }
        catch (final Exception exc)
        {
            throw new FileSystemException("vfs.provider.sftp/connect.error", new Object[]{hostname}, exc);
        }


        return session;
    }
    
    /**
     * Finds the .ssh directory.
     * <p>The lookup order is:</p>
     * <ol>
     * <li>The system property <code>vfs.sftp.sshdir</code> (the override
     * mechanism)</li>
     * <li><code>{user.home}/.ssh</code></li>
     * <li>On Windows only: C:\cygwin\home\{user.name}\.ssh</li>
     * <li>The current directory, as a last resort.</li>
     * <ol>
     * <p/>
     * Windows Notes:
     * The default installation directory for Cygwin is <code>C:\cygwin</code>.
     * On my set up (Gary here), I have Cygwin in C:\bin\cygwin, not the default.
     * Also, my .ssh directory was created in the {user.home} directory.
     * </p>
     *
     * @return The .ssh directory
     */
    private static File findSshDir()
    {
        String sshDirPath;
        sshDirPath = System.getProperty("vfs.sftp.sshdir");
        if (sshDirPath != null)
        {
            File sshDir = new File(sshDirPath);
            if (sshDir.exists())
            {
                return sshDir;
            }
        }

        File sshDir = new File(System.getProperty("user.home"), SSH_DIR_NAME);
        if (sshDir.exists())
        {
            return sshDir;
        }

        if (Os.isFamily(Os.OS_FAMILY_WINDOWS))
        {
            // TODO - this may not be true
            final String userName = System.getProperty("user.name");
            sshDir = new File("C:\\cygwin\\home\\" + userName + "\\" + SSH_DIR_NAME);
            if (sshDir.exists())
            {
                return sshDir;
            }
        }
        return new File("");
    }
}

