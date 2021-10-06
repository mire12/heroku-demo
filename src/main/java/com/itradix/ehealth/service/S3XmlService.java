package com.itradix.ehealth.service;


import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class S3XmlService extends S3StorageService {



    public void removeImageFromAmazon(String methodName) {
        //String fileName = amazonImage.getImageUrl().substring(amazonImage.getImageUrl().lastIndexOf("/") + 1);
        getClient().deleteObject(new DeleteObjectRequest(getBucketName(), methodName));
        //amazonImageRepository.delete(amazonImage);
    }

    // Send image to AmazonS3, if have any problems here, the image fragments are removed from amazon.
    // Font: https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#putObject%28com.amazonaws.services.s3.model.PutObjectRequest%29
    public void uploadPublicFile(String fileName, File file) {
        getClient().putObject(new PutObjectRequest(getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
    
    public byte[] downloadPublicFile(String evID, String filename) throws IOException {
        com.amazonaws.services.s3.model.S3Object obj = getClient().getObject("nczi-preprod", evID + filename + ".xml");
        System.out.println(obj.getObjectMetadata());
        return obj.getObjectContent().readAllBytes();
    }

}
