package s3

import java.io.IOException
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, CreateBucketRequest, GetBucketLocationRequest}
import java.io.File
import play.api.Logger

case class S3(){
  //private static String bucketName     = "*** bucket name ***";
//  private static String bucketName     = "*** Provide bucket name ***";
//  private static String keyName        = "*** Provide key ***";
//  private static String uploadFileName = "*** Provide file name ***";
  val keyName = ""

  def createBucket(bucketName:String) = {
    val s3client = new AmazonS3Client(new ProfileCredentialsProvider())
    s3client.setRegion(Region.getRegion(Regions.US_EAST_1))

    try {
      if(!(s3client.doesBucketExist(bucketName)))
      {
        // Note that CreateBucketRequest does not specify region. So bucket is
        // created in the region specified in the client.
        s3client.createBucket(new CreateBucketRequest(bucketName))
      }
      // Get location.
      val bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName))
      Logger.info("bucket location = " + bucketLocation)

    } catch {
      case ase: AmazonServiceException =>
        Logger.info("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.")
        Logger.info("Error Message:    " + ase.getMessage())
        Logger.info("HTTP Status Code: " + ase.getStatusCode())
        Logger.info("AWS Error Code:   " + ase.getErrorCode())
        Logger.info("Error Type:       " + ase.getErrorType())
        Logger.info("Request ID:       " + ase.getRequestId())
      case ace: AmazonClientException =>
        Logger.info("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.")
        Logger.info("Error Message: " + ace.getMessage())
    }
  }

  def upload(bucketName:String, uploadFileName:String, file:File) {

      val s3client = new AmazonS3Client(new ProfileCredentialsProvider())
      try {
        Logger.info("Uploading a new object to S3 from a file\n")
        val file = new File(uploadFileName)
        s3client.putObject(new PutObjectRequest(bucketName, keyName, file))
      } catch {
        case ase:AmazonServiceException =>
          Logger.info("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.")
          Logger.info("Error Message:    " + ase.getMessage())
          Logger.info("HTTP Status Code: " + ase.getStatusCode())
          Logger.info("AWS Error Code:   " + ase.getErrorCode())
          Logger.info("Error Type:       " + ase.getErrorType())
          Logger.info("Request ID:       " + ase.getRequestId())
        case ace:AmazonClientException =>
          Logger.info("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.")
          Logger.info("Error Message: " + ace.getMessage())
        }
  }
}
