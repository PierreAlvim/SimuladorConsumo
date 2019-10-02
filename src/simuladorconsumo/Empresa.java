/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

/**
 *
 * @author Pierre
 */
public class Empresa {
    protected String nome;
    protected String endereco;
    protected String tipo;

    public Empresa() {
    }

    public Empresa(String nome, String endereco, String tipo) {
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Empresa{" + "nome=" + nome + ", endereco=" + endereco + ", tipo=" + tipo + '}';
    }
    
    
}
