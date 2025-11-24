package com.yxx.common.core.domain.dto;

import com.yxx.common.utils.file.FileUploadUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * {@link com.yxx.web.controller.common.CommonController#uploadFile}
 */
public class FileUploadDTO implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * "http://domain:port/profile/upload/20xx/xx/xx/xxx_xxxx180031A066.pdf"
     */
    private String url;

    /**
     * /profile/upload/20xx/xx/xx/xxx_xxxx180031A066.pdf
     */
    private String filePath;

    /**
     * /profile/upload/20xx/xx/xx/xxx_xxxx180031A066.pdf
     */
    private String fileName;

    /**
     * 本地路径
     */
    private String localPath;

    /**
     * "xxx_xxxx180031A066.pdf"
     */
    private String newFileName;

    /**
     * "xxxx.pdf"
     */
    private String originalFilename;

    /**
     * "pdf"
     */
    private String fileType;

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getLocalPath()
    {
        return "D:/yxx/project/yxx-yi/yxx-yxx-17/yxx/uploadPath" + filePath.replaceFirst("/profile", "");
    }

    public void setLocalPath(String localPath)
    {
        this.localPath = localPath;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getNewFileName()
    {
        return newFileName;
    }

    public void setNewFileName(String newFileName)
    {
        this.newFileName = newFileName;
    }

    public String getOriginalFilename()
    {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename)
    {
        this.originalFilename = originalFilename;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }
}
