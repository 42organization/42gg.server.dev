package io.pp.arcade.v1.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Arrays;

@Component
public class UserImageHandler {
    private final AmazonS3 amazonS3;
    private final FileDownloader fileDownloader;

    public UserImageHandler(AmazonS3 amazonS3, FileDownloader fileDownloader) {
        this.amazonS3 = amazonS3;
        this.fileDownloader = fileDownloader;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    @Value("${info.image.defaultUrl}")
    private String defaultImageUrl;

    public String uploadAndGetS3ImageUri(String intraId, String imageUrl) {
        if (!isStringValid(intraId) || !isStringValid(imageUrl)) {
            return defaultImageUrl;
        }
        byte[] downloadedImageBytes = fileDownloader.downloadFromUrl(imageUrl);
        try {
            byte[] resizedImageBytes = ImageResizingUtil.resizeImageBytes(downloadedImageBytes, 0.5);
            MultipartFile multipartFile = new JpegMultipartFile(resizedImageBytes, intraId);
            return uploadToS3(multipartFile);
        } catch (IOException e) {
            return defaultImageUrl;
        }
    }

    private Boolean isStringValid(String intraId) {
        return intraId != null && intraId.length() != 0;
    }

    public String uploadToS3(MultipartFile multipartFile) throws IOException {
        try {
            String s3FileName = dir + multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getSize());
            amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, inputStream, objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucketName, s3FileName).toString();
        } catch (Exception e) {
            return defaultImageUrl;
        }
    }
}
