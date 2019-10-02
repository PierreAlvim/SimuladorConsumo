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
public class ParametrosConsumo {
    public static double TARIFA_DISTR = 0.78570090;
    public static double DESCONTO = 0.15;
    public static double TARIFA_GERA = (TARIFA_DISTR * (1-DESCONTO));
    //public static Empresa empresaGeradora = new Empresa("Zecon", "Santos Dummond", "Geradora");
    
    public static void atualizaTarifaGeracao()
    {
        TARIFA_GERA = (TARIFA_DISTR * (1-DESCONTO));
    }
    
    public enum TipoRedeEletrica{
        MONOFASICA(1,30.),BIFASICA(2,50.),TRIFASICA(3,100.);
        
        private final int fases;
        private Double custoDispo; //Custo de disponibilidade em kWh/mes

        TipoRedeEletrica(int fases, Double custoDispo){
                this.fases = fases;
                this.custoDispo = custoDispo;
        }
        
        void setCustoDispo(Double custoDispo)
        {
            this.custoDispo =custoDispo;
        }
        
        Double getCustoDisponibilidade(){
            return custoDispo;
        }
        
    }
}
