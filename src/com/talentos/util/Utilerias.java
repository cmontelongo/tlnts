package com.talentos.util;

import java.util.Calendar;

public class Utilerias {
	
	public static String[] obtenerDiasMes(int mesBuscado, int[] diasSemana){
		String[] diasMes = new String[31];
		if (diasSemana.length>0){
			Calendar fechaActual = Calendar.getInstance();
			int diaActual = 0;
			if (mesBuscado >= 0)
				fechaActual.set(fechaActual.get(Calendar.YEAR), mesBuscado, 1);
			else
				mesBuscado = fechaActual.get(Calendar.MONTH);
	
			while (fechaActual.get(Calendar.MONTH)==mesBuscado){
				for (int i=0; i<diasSemana.length; i++){
					if (diasSemana[i]==fechaActual.get(Calendar.DAY_OF_WEEK)){
						diasMes[diaActual] = Constantes.sDiaSemanaC[diasSemana[i]] + " " + fechaActual.get(Calendar.DAY_OF_MONTH);
						diaActual ++;
					}
				}
				fechaActual.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		return diasMes;
	}

}
