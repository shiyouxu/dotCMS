package com.dotmarketing.viewtools.content;

import java.io.File;
import java.io.IOException;

import com.dotcms.repackage.org.apache.commons.lang.builder.ToStringBuilder;

import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.structure.model.Field;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.util.FileUtil;

/**
 * A helper class to provide an object to return to the dotCMS when a content has a binary field
 * @since 1.9.1.3
 * @author Jason Tesser
 *
 */
public class BinaryMap {

	private String name;
	private String size;
	private String rawUri;
	private String resizeUri;
	private String thumbnailUri;
	private Contentlet content;
	private Field field;
	private File file;
	
	public BinaryMap(Contentlet content, Field field) {
		this.content = content;
		this.field = field;
		try {
			file = content.getBinary(field.getVelocityVarName());
		} catch (IOException e) {
			Logger.error(this, "Unable to retrive binary file for content id " + content.getIdentifier() + " field " + field.getVelocityVarName(), e);
		}
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).
	       append("name", getName()).
	       append("size", getSize()).
	       append("rawUri", getRawUri()).
	       append("resizeUri", getResizeUri()).
	       append("thumbnailUri", getThumbnailUri()).
	       append("file", getFile()).
	       toString();
	}

	/**
	 * The size of the actual raw file
	 * @return the size of the file
	 */
	public String getSize() {
		if(file != null) {
			return FileUtil.getsize(file);
		}
		return null;
	}

	/**
	 * The name of the file
	 * @return the name
	 */
	public String getName() {
		if(file != null) {
			return file.getName();
		}
		return "";
	}

	/**
	 * The rawURI is link to the actual full image
	 * @return the rawUri
	 */
	public String getRawUri() {
		rawUri = getName().length()>0? UtilMethods.espaceForVelocity("/contentAsset/raw-data/"+content.getIdentifier()+"/"+ field.getVelocityVarName()):"";
		return rawUri;
	}

	/**
	 * This is a convince method/helper method to build the URI for the resize exporter.  
	 * You may pass any of the parameters as null, or 0 
	 * <br/>
	 * Either width or height are required, if one of those is not passed then the image will be resized proportionally to preserve its aspect ratio,  
	 * otherwise both parameters will be used the image could look stretched.
	 * @return the resizeUri
	 */
	public String getResizeUri() {
	    if(getName().length()==0) return "";
		resizeUri = "/contentAsset/image/"+content.getInode()+"/"+field.getVelocityVarName()+"/byInode/1/filter/Resize"; 
		return resizeUri;
	}
	
	/**
	 * Either width or height are required, if one of those is not passed then the image will be resized proportionally to preserve its aspect ratio,  
	 * otherwise both parameters will be used the image could look stretched.
	 * 
	 * @param width Width to resize to
	 * @param height Height to resize to
	 */
	public String getResizeUri(Integer width, Integer height){
	    if(getName().length()==0) return "";
	    StringBuilder uri=new StringBuilder();
	    uri.append(getResizeUri());
	    if(width!=null && width>0)
	        uri.append("/resize_w/").append(width);
	    if(height!=null && height>0)
	        uri.append("/resize_h/").append(height);
	    return uri.toString();
	}
	
	/**
	 * Either w (width) or h (height) are required, if one of those is not passed then the image will be resized proportionally to preserve its aspect ratio, and the
	 * background color parameter will not be used
	 * If both width and height are passed, then the background color parameter could be used to preserver the aspect ratio and generate bands to respect
	 * the passed width and height, if no background color is passed then white will be used by default.
	 */
	public String getThumbnailUri() {
	    if(getName().length()==0) return "";
        resizeUri = "/contentAsset/image/"+content.getInode()+"/"+field.getVelocityVarName()+"/byInode/1/filter/Thumbnail"; 
        return resizeUri;
	}

	/**
	 * This is a convince method/helper method to build the URI for the thumbnail exporter.  
	 * You may pass any of the parameters as null, empty or 0 in the case of ints.
	 * <br/>
	 * Either w (width) or h (height) are required, if one of those is not passed then the image will be resized proportionally to preserve its aspect ratio, and the
	 * background color parameter will not be used
	 * If both width and height are passed, then the background color parameter could be used to preserver the aspect ratio and generate bands to respect
	 * the passed width and height, if no background color is passed then white will be used by default.
	 * 
	 * @param width Width to resize to
	 * @param height Height to resize to
	 * @param background RGB so ie.. WHITE is 255255255
	 * @return
	 */
	public String getThumbnailUri(Integer width, Integer height, String background){
	    if(getName().length()==0) return "";
        StringBuilder uri=new StringBuilder();
        uri.append(getThumbnailUri());
        if(width!=null && width>0)
            uri.append("/thumbnail_w/").append(width);
        if(height!=null && height>0)
            uri.append("/thumbnail_h/").append(height);
        if(background!=null)
            uri.append("/thumbnail_bg/").append(background);
        return uri.toString();
	}
	
	/**
	 * This is the underneath Java File.  Becareful when working with this object as you can manipulate it. 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
}