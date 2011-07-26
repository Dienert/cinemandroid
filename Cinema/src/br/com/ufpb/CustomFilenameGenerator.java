/**
 * Manage Paths of resources on Red5
 */
package br.com.ufpb;

import org.red5.server.api.IScope;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.stream.IStreamFilenameGenerator;

public class CustomFilenameGenerator implements IStreamFilenameGenerator {

    /** Path that will store recorded videos. */
    public static String recordPath;
    /** Path that contains VOD streams. */
    public static String playbackPath;
    
    /**
     * Retrieve the path of directory where are stored the videos
     * @param scope
     * @return fullpath 
     */
    private String getStreamDirectory(IScope scope) {
		final StringBuilder result = new StringBuilder();
		final IScope app = ScopeUtils.findApplication(scope);
		while (scope != null && scope != app) {
			result.insert(0, "/" + scope.getName());
			scope = scope.getParent();
		}
		return playbackPath + result.toString();
	}
    
	/**
	 * Generate a filename without an extension.
	 * 
	 * @param scope           Scope to use
	 * @param name            Stream name
     * @param type            Generation strategy (either playback or record)
     * @return                Full filename
	 */
	public String generateFilename(IScope scope, String name, GenerationType type) {
		return generateFilename(scope, name, null, type);
	}
	
	/**
	 * Generate a filename with an extension.
	 *
	 * @param scope           Scope to use
	 * @param name            Stream filename
	 * @param extension       Extension
     * @param type            Generation strategy (either playback or record)
	 * @return                Full filename with extension
     */
	public String generateFilename(IScope scope, String name, String extension, GenerationType type) {
		String filename;    
		filename = getStreamDirectory(scope) + name;
        
        if (extension != null)
            // Add extension
            filename += extension;
        
        return filename;
	}
	
	/**
     * True if returned filename is an absolute path, else relative to application.
     * 
     * If relative to application, you need to use
     * <code>scope.getContext().getResources(fileName)[0].getFile()</code> to resolve
     * this to a file.
     * 
     * If absolute (ie returns true) simply use <code>new File(generateFilename(scope, name))</code>
     * 
     * @return
     */
    public boolean resolvesToAbsolutePath() {
    	return true;
    }
   
    /**
     * path to playback
     * @param path
     */
    public void setPlaybackPath(String path) {
    	playbackPath = path;
    }
   
    /**
     * path to record
     * @param path
     */
    public void setRecordPath(String path) {
        recordPath = path;
    }

}

