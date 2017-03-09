package group22.android.com.group22.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import group22.android.com.group22.GraphActivity;


public class UploadAsync extends AsyncTask<Void, Void, Void> {
    public Context context;

    public UploadAsync(Context context) {
        this.context = context;
    }


    int serverResponseCode = 0;
    final String srcFile = "/data/user/0/group22.android.com.group22/databases/Group22.db";
    String upLoadServerUri = "https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php";

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Uploading", "Please wait ...", true);
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        fileUpload();
        return null;
    }


    private void fileUpload() {
        String fileName = srcFile;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(srcFile);


        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (!sourceFile.isFile()) {
            Log.d("uploadFile", "Source File does not exist :");

        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                conn = (HttpsURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;" + "filename="
                        + "Group22.db" + lineEnd);

                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                serverResponseCode = conn.getResponseCode();

                if (serverResponseCode == 200) {
                    Log.d("UploadService", "File Uploaded Successfully");
                }

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file to serve", "Exception : "
                        + e.getMessage(), e);
            }
        }
    }
}