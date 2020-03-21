package com.demo.gis.common.toolUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author hubin
 * @since 2017-02-16
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static byte[] buffer = new byte[2048];

    /**
     * 获取项目路径
     *
     * @return
     */
    public static String getAbsPathOfProject(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("//");
    }

    /**
     * 获取项目文件流路径
     *
     * @return
     */
    public static InputStream getResourceAsStreamOfProject(HttpServletRequest request, String path) {
        return request.getSession().getServletContext().getResourceAsStream(path);
    }

    /**
     * 创建目录
     *
     * @param dirPath
     * @return
     */
    public static boolean makeDir(String dirPath) {
        return makeDir(new File(dirPath));
    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean makeDir(File file) {
        return !(!file.exists() && !file.isDirectory()) || file.mkdirs();
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFilePrefix(String fileName) {
        if (null != fileName && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取文件上级目录
     *
     * @param path 文件路径
     * @return
     */
    public static String getFileParentPath(String path) {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            return path.substring(0, path.lastIndexOf("/") + 1);
        }
        return null;
    }

    /**
     * 获取项目请求路径
     *
     * @param request
     * @return
     * @author Caratacus
     */
    public static String reqPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();
    }

    /**
     * 计算文件大小(适用于计算文件及文件夹大小)
     *
     * @param file 文件
     * @return 返回long类型字节(B)
     */
    public static long size(File file) {
        if (file.exists()) {
            return file.length();
        }
        return 0L;
    }

    /**
     * 计算文件大小
     *
     * @param filePath 文件路径
     * @return 返回long类型字节(B)
     */
    public static long size(String filePath) {
        return size(new File(filePath));
    }



    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (ArrayUtils.isNotEmpty(children)) {
                // 递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * <p>
     * 文件删除
     * </p>
     *
     * @param files
     */
    public static void deleteFiles(File... files) {
        deleteFiles(Arrays.asList(files));
    }

    public static void deleteFiles(Collection<File> files) {
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (null != file && file.isFile()) {
                boolean success = file.delete();
                if (!success) {
                    // 记录删除失败的文件
                    logger.debug("临时文件删除失败:[{}]", file.getPath());
                } else {
                    logger.debug("临时文件删除成功:[{}]", file.getPath());
                }
            }
        }
    }

    public static HttpServletResponse downLoadFiles(List<File> files, HttpServletResponse response) {
        return downLoadFiles(files, null, response);
    }

    /**
     * <p>
     * 打包下载多个文件，可以指定zip的名字
     * </p>
     *
     * @param files
     * @param zipName
     * @param response
     * @return
     */
    public static HttpServletResponse downLoadFiles(List<File> files, String zipName, HttpServletResponse response) {

        try {
            //List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
            //创建一个临时压缩文件

            //临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤
            File file;
            if (StringUtils.isNotEmpty(zipName)) {
                file = new File(System.getProperty("java.io.tmpdir") + File.separator + zipName);
            } else {
                file = File.createTempFile("gene", ".zip");
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteFiles(files);
        }
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     *
     * @param
     * @param
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param
     * @param
     */
    public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        if (!file.exists()) {
            System.out.println("待压缩的文件目录：" + file + "不存在.");
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");

                //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                response.setHeader("Content-Disposition", "attachment;filename="
                        + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public static void download(File file, HttpServletResponse response) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            download(is, file.getName(), response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(is);
        }
    }

    public static void download(InputStream is, String fileName, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(is);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static File productFileByInputStream(InputStream f, String last) throws IOException {
        File tempfile = File.createTempFile("temp", "." + last);//创建临时文件
        FileOutputStream outputStream = new FileOutputStream(tempfile);//输出流对象，指定输出指tempfile目录下
        byte b[] = new byte[1024];
        int n;
        while ((n = f.read(b)) != -1) {
            //从输出流中每次读取1024字节,直至读完
            outputStream.write(b, 0, n);
        }
        outputStream.close();
        f.close();//关闭输入输出流
        return tempfile;
    }

    /**
     * 关闭流 删除文件列表
     *
     * @param is
     * @param deleteList
     */

//    public static void deleteFileListInputStreamClose(InputStream is, List<File> deleteList) {
//        try {
//            is.close();
//            if (CollectionUtils.isNotEmpty(deleteList)) {
//                for (File f : deleteList) {
//                    f.delete();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * <p>
     * 将文件解压到指定的目录
     * </p>
     *
     * @param zipFile 压缩文件
     * @param outDir  解压目录
     * @throws IOException
     */
    public static void unZip(File zipFile, String outDir) throws IOException {
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                logger.info("创建压缩目录成功!");
            } else {
                logger.info("创建压缩目录失败!");
            }
        } else if (outFileDir.isFile()) {
//           Assert.fail("outDir 不能是文件");
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration enumeration = zip.entries(); enumeration.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);

            if (entry.isDirectory()) {
                //处理压缩文件包含文件夹的情况
                File fileDir = new File(outDir + zipEntryName);
                fileDir.mkdir();
                continue;
            }

            File file = new File(outDir, zipEntryName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * <p>
     * 打包文件或者文件夹
     * 可以保持目录结构
     * </p>
     *
     * @param out        zip输出流
     * @param sourceFile 源文件或目录，需要打包的
     * @param base       指定sourceFile的文件名,因为是递归打包，所以不能文件内部直接获取sourceFile的文件名，需要外部指定
     * @throws Exception
     */
    public static void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if (flist.length == 0) {
                //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                System.out.println(base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else {
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++) {
                    compress(out, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else {
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            System.out.println(base);
            //将源文件写入到zip文件中
            while ((tag = bis.read(buffer)) != -1) {
                out.write(buffer, 0, tag);
            }
            bis.close();
            fos.close();

        }
    }

    /**
     * 判断 path 的父级目录不存在的话就创建
     *
     * @param path
     */
    public static void mkdirsIfNotExists(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        File parentFile = new File(path).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    /**
     * 下载文件到服务器本地
     *
     * @param is
     * @param destPath
     * @throws IOException
     */
    public static void downloadFileLocal(InputStream is, String destPath) throws IOException {
        // 万一路径没有呢
        mkdirsIfNotExists(destPath);
        OutputStream os = new FileOutputStream(destPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
        int length;
        byte[] buffer = new byte[2048];
        while ((length = is.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, length);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        os.close();
        is.close();
    }

    /**
     * 下载文件到服务器本地
     *
     * @param data     这个是文件 byte 数组
     * @param destPath 这个是目标文件的全路径名
     * @throws IOException 抛异常, 假如有的话
     */
    public static void downloadFileLocal(byte[] data, String destPath) throws IOException {
        // 万一路径没有呢
        mkdirsIfNotExists(destPath);
        OutputStream os = new FileOutputStream(destPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
        bufferedOutputStream.write(data);
        bufferedOutputStream.close();
        os.flush();
        os.close();
    }



}
