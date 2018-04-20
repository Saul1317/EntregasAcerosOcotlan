package com.acerosocotlan.entregasacerosocotlan.modelo;

/**
 * Created by Saul on 08/03/2018.
 */

public class ConvertidorFecha {
    String ano, dia, mes,diaTexto;

    public String ConvertirFecha(String fechaHora){
        String[] fechaHoraTotal = fechaHora.split(" ");
        String fechaTotal = fechaHoraTotal[0];
        String[] fecha = fechaTotal.split("-");
        ano = fecha[0];
        mes = fecha[1];
        dia = fecha[2];

        if(mes.equals("01")){
            mes= "Enero";
        }
        else if(mes.equals("02")){
            mes= "Febrero";
        }
        else if(mes.equals("03")){
            mes= "Marzo";
        }
        else if(mes.equals("04")){
            mes= "Abril";
        }
        else if(mes.equals("05")){
            mes= "Mayo";
        }
        else if(mes.equals("06")){
            mes= "Junio";
        }
        else if(mes.equals("07")){
            mes= "Julio";
        }
        else if(mes.equals("08")){
            mes= "Agosto";
        }
        else if(mes.equals("09")){
            mes= "Septiembre";
        }
        else if(mes.equals("10")){
            mes= "Octubre";
        }
        else if(mes.equals("11")){
            mes= "Noviembre";
        }
        else if(mes.equals("12")){
            mes= "Diciembre";
        }
        return dia+" de "+mes+" del "+ano;
    }
}
