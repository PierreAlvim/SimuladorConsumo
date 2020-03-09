/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasejson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import simuladorconsumo.EmpresaCliente;

/**
 *
 * @author Pierre
 */
public class JsonIO {

    private static final String FILEPREFIX = "data_";
    private static final String FILEEXTENSION = ".json";
    /**
     * Caminho raiz para salvar os arquivos .json.
     */
    private static final String FILEROOT = "dados_clientes/";

    /**
     * Salva os dados de consumo e os da empresa em um arquivo JSON no caminho
     * {@linkplain #FILEROOT}.
     * <br>Nome do arquivo será:
     * {@linkplain #FILEPREFIX} + {@linkplain EmpresaCliente#nome} + {@linkplain #FILEEXTENSION}.
     *
     * @param emp Objeto a ser salvo.
     * @throws IOException
     */
    public static void save(EmpresaCliente emp) throws IOException {
        Gson g = new Gson();
        //g.toJson(list);
        File file = new File(FILEROOT);

        if (!file.isDirectory()) {
            file.mkdir();
        }
        if (!file.isDirectory()) {
            throw new IOException("Can't create Directory");

        } else {
            Writer writer = null;
            file = new File(file.getPath() + "/" + FILEPREFIX + emp.getNome() + FILEEXTENSION);

            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8"));
            writer.write(g.toJson(emp.toSimpleData()));
            writer.close();

        }
    }

    /**
     * Carrega objeto {@linkplain EmpresaCliente} de um arquivo .json
     * previamente salvo.<br> O objeto lido inicialmente é do tipo
     * {@linkplain SimpleDataConsumo}, que depois é convertido para o tipo
     * {@linkplain EmpresaCliente}.<br>O Arquivo só contém os dados de consumo e
     * dados da empresa, nada sobre a simulação é salvo.
     *
     * @param file Arquivo a ser lido.
     * @return Objeto {@linkplain EmpresaCliente} ou {@code null} caso não
     * consiga ler o arquivo ou interpretá-lo.
     */
    public static EmpresaCliente loadFile(File file) {

        Gson g = new Gson();
        BufferedReader reader = null;
        String s = "";
        try {

            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "utf-8"));
            s = reader.readLine();

        } catch (IOException ex) {
            // Report
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        SimpleDataConsumo sd = null;
        if (!"".equals(s)) {
            sd = g.fromJson(s, SimpleDataConsumo.class);
        }
        return sd.toEmpCliente();
    }

    public static List<File> findData() {
        File file = new File(FILEROOT);
        if (file.isDirectory()) {
            File afile[] = file.listFiles((File f) -> {
                if (f.getName().startsWith(FILEPREFIX) && f.getName().endsWith(FILEEXTENSION)) {
                    return true;
                } else {
                    return false;
                }
            });
            return Arrays.asList(afile);
        }
        return new ArrayList();

    }
}
