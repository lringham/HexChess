package Util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Screen 
{
	private static boolean fullscreen = false;
	private static boolean vsync = true;

	public Screen(int width, int height, String title)
	{
        try
        {    		
    		Display.setVSyncEnabled(vsync);
        	Display.setDisplayMode(new DisplayMode(width,height));
            Display.setFullscreen(fullscreen);
    		Display.setTitle(title);
	    	Display.create();
        } 
        catch (LWJGLException e) 
        {
        	e.printStackTrace();
        	System.exit(0);
        }
	}

	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public static void setDisplayMode(int width, int height, boolean fullscreen) 
	{
		Screen.fullscreen = fullscreen;
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = null;
			
		if (fullscreen) {
		    DisplayMode[] modes = Display.getAvailableDisplayModes();
		    int freq = 0;
					
		    for (int i=0;i<modes.length;i++) {
		        DisplayMode current = modes[i];
						
			if ((current.getWidth() == width) && (current.getHeight() == height)) {
			    if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			        if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
				    targetDisplayMode = current;
				    freq = targetDisplayMode.getFrequency();
	                        }
	                    }

			    // if we've found a match for bpp and frequence against the 
			    // original display mode then it's probably best to go for this one
			    // since it's most likely compatible with the monitor
			    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }

	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }

	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
				
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
	
	
	public static void setFullscreen(boolean fullscreen)
	{
		setDisplayMode(getWidth(), getHeight(), fullscreen);
	}
	
	public static void setVsync(boolean vsync)
	{
		Screen.vsync = vsync;
		Display.setVSyncEnabled(vsync);
	}
	
	public static void setSize(int width, int height)
	{
		setDisplayMode(width, height, fullscreen);
	}
	
	public static int getWidth()
	{
		return Display.getWidth();
	}
	
	public static int getHeight()
	{
		return Display.getHeight();
	}
	
	public static void setTile(String title)
	{
		Display.setTitle(title);
	}
	
	public static String getTile()
	{
		return Display.getTitle();
	}
}
