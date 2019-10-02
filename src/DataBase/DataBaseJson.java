/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import simuladorconsumo.EmpresaCliente;
import simuladorconsumo.InfoMensal;

/**
 *
 * @author Pierre
 */
public class DataBaseJson {
    
    private static final String FILEPREFIX = "dataCli";

    public static void save(EmpresaCliente emp) {
        Gson g = new Gson();
        //g.toJson(list);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FILEPREFIX + emp.getNome() + ".json"), "utf-8"));
            writer.write(g.toJson(emp));
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {/*ignore*/
            }
        }

    }

    public static EmpresaCliente loadFile(File file) {

        Gson g = new Gson();
        BufferedReader reader = null;
        String s = "";
        try {

            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "utf-8"));
            JsonReader jRead = g.newJsonReader(reader);
            s = reader.readLine();

        } catch (IOException ex) {
            // Report
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {/*ignore*/
            }
        }

        EmpresaCliente e = g.fromJson(s, EmpresaCliente.class);

        return e;        
    }
    
    public static List<File> findData(String root){
        File file = new File(root);
        File afile[] = file.listFiles((File f) -> {
            if (f.getName().startsWith(FILEPREFIX) && f.getName().endsWith(".json"))
                return true; //To change body of generated lambdas, choose Tools | Templates.
            else
                return false;
        });

        return Arrays.asList(afile);
    }
}
