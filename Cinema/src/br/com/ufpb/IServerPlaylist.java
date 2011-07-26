package br.com.ufpb;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IServerStream;

public interface IServerPlaylist {
	
	public void start(boolean alreadyPlaying);
	public void stop();
	public void setNewSchedule();
	public void changeSchedule();
	
	public void shutdown();
	public void init();
	public void init(IScope scope);
	
	public void setScope(IScope scope);
	public void setRepeat(Boolean repeat);
	public void setPath(String path);
	public void setPattern(String pattern);
	public void setStreamName(String name);
	public void setRunOnStart(Boolean value);
	public IServerStream getStreamServer();
	public Boolean getRunOnStart();
	
	public boolean isStarted();
}
