package com.tranred.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;

public class Archivo {
    private File archivo;
    private FileWriterWithEncoding fw;
    private OutputStream os;
    private Charset oscs;

    public Archivo() {
    }

    public Archivo(File archivo) {
        this.archivo = archivo;
    }

    public Archivo(String archivo) {
        this.archivo = new File(archivo);
    }

    public File getArchivo() {
        return this.archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public void setOutputStreamCharset(String encoder) {
        this.oscs = Charset.forName(encoder);
    }

    public List<String> leer() {
        ArrayList data = new ArrayList();

        try {
            Scanner s = new Scanner(this.archivo);

            while(s.hasNextLine()) {
                data.add(s.nextLine());
            }

            s.close();
            return data;
        } catch (FileNotFoundException var4) {
            return data;
        }
    }

    public void openFileWriter(Boolean append) throws IOException {
        this.fw = new FileWriterWithEncoding(this.archivo, StandardCharsets.ISO_8859_1, append);
    }

    public void openFileWriter(Charset cs, Boolean append) throws IOException {
        this.fw = new FileWriterWithEncoding(this.archivo, cs, append);
    }

    public void openOutputStream(Boolean append) throws IOException {
        this.os = new FileOutputStream(this.archivo, append);
    }

    public void closeFileWriter() throws IOException {
        this.fw.close();
    }

    public void closeOutputStream() throws IOException {
        this.os.close();
    }

    public void escribir(String linea) throws IOException {
        this.fw.append(linea + System.getProperty("line.separator"));
        this.fw.flush();
    }

    public void escribir(Integer length) throws IOException {
        this.fw.append(StringUtils.repeat(" ", length) + System.getProperty("line.separator"));
        this.fw.flush();
    }

    public void escribirEBCDIC(String linea) throws IOException {
        this.os.write(linea.getBytes(this.oscs));
        this.os.write(System.getProperty("line.separator").getBytes());
    }

    public void deleteWorkFiles(String ruta) {
        File f = new File(ruta);
        FilenameFilter acpFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".acp") || lowercaseName.endsWith(".dec");
        };
        File[] files = f.listFiles(acpFilter);
        File[] var5 = files;
        int var6 = files.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            File file = var5[var7];
            if (!file.isDirectory() && !file.delete()) {
                System.err.println("Can't remove " + file.getAbsolutePath());
            }
        }

    }

    public static void makeDirectory(String ruta) {
        File arch = new File(ruta);
        if (!arch.exists() && !arch.mkdir()) {
            arch.mkdirs();
        }

    }

    public Boolean moveFile(String directoryPath) throws IOException {
        File path = new File(directoryPath);
        makeDirectory(directoryPath);
        return !this.archivo.exists() ? Boolean.FALSE : Files.move(this.archivo.toPath(), path.toPath().resolve(this.archivo.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING).toFile().exists();
    }

    public Boolean copyFile(String directoryPath) throws IOException {
        File path = new File(directoryPath);
        makeDirectory(directoryPath);
        return !this.archivo.exists() ? Boolean.FALSE : Files.copy(this.archivo.toPath(), path.toPath().resolve(this.archivo.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING).toFile().exists();
    }

    public void renameFile(File arc) {
        if (this.archivo.renameTo(arc)) {
            this.archivo = arc;
        }

    }

    public static void copyFiles(String path, Archivo... files) throws IOException {
        Archivo[] var2 = files;
        int var3 = files.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Archivo arc = var2[var4];
            arc.copyFile(path);
            arc.getArchivo().delete();
        }

    }
}
