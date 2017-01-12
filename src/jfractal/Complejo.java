/*
 * Copyright (C) 2016 SamuelCoral
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jfractal;

import java.awt.Color;

/**
 *
 * @author SamuelCoral
 */
public class Complejo {
    
    public double real;
    public double imaginario;
    
    public Complejo() {
        
        real = imaginario = 0;
    }
    
    public Complejo(double real) {
        
        this.real = real;
        imaginario = 0;
    }
    
    public Complejo(double real, double imaginario) {
        
        this.real = real;
        this.imaginario = imaginario;
    }
    
    public Complejo(Complejo origen) {
        
        real = origen.real;
        imaginario = origen.imaginario;
    }
    
    //--------------------------funciones que modifican el valor----------------
    
    public void sumar(Complejo a) {
        
        real += a.real;
        imaginario += a.imaginario;
    }
    
    public void restar(Complejo a) {
        
        real -= a.real;
        imaginario -= a.imaginario;
    }
    
    public void multiplicar(Complejo a) {
        
        Complejo resultado = new Complejo(real * a.real - imaginario * a.imaginario, real * a.imaginario + imaginario * a.real);
        real = resultado.real;
        imaginario = resultado.imaginario;
    }
    
    public void dividir(Complejo a) {
        
        Complejo inverso = new Complejo(a.real/(a.real*a.real + a.imaginario*a.imaginario), - a.imaginario/(a.real*a.real + a.imaginario*a.imaginario));
        
        Complejo resultado = new Complejo(real * inverso.real - imaginario * inverso.imaginario, real * inverso.imaginario + imaginario * inverso.real);
        real = resultado.real;
        imaginario = resultado.imaginario;
    }
    
    public void potenciaDeBaseReal(Double a) {
        if(a == 0){
            real = 0;
            imaginario = 0;
        }
        else{
            Complejo resultado = new Complejo( Math.pow(Math.E, real * Math.log(a)) * Math.cos(imaginario * Math.log(a)) , Math.pow(Math.E, real * Math.log(a)) * Math.sin(imaginario * Math.log(a)) );
            real = resultado.real;
            imaginario = resultado.imaginario;
        }
    }
    
    public void elevarAEntero (int a){
        Complejo resultado = this.aLa(a);
        this.real = resultado.real;
        this.imaginario = resultado.imaginario;
    }
    
    //---------------------------funciones encadenables-------------------------
    
    public Complejo mas(Complejo a) {
        Complejo resultado = new Complejo(real + a.real, imaginario + a.imaginario);
        return resultado;
    }
    public Complejo menos(Complejo a) {
        Complejo resultado = new Complejo(real - a.real, imaginario - a.imaginario);
        return resultado;
    }
    public Complejo por(Complejo a) {
        Complejo resultado = new Complejo(real * a.real - imaginario * a.imaginario, real * a.imaginario + imaginario * a.real);
        return resultado;
    }
    public Complejo entre(Complejo a) {
        Complejo inverso = a.inverso();
        Complejo resultado = this.por(inverso);
        return resultado;
    }
    public Complejo elevandoBaseReal(Double a) {
        if(a == 0){
            return new Complejo(0,0);
        }
        else{
            Complejo resultado = new Complejo( Math.pow(Math.E, real * Math.log(a)) * Math.cos(imaginario * Math.log(a)) , Math.pow(Math.E, real * Math.log(a)) * Math.sin(imaginario * Math.log(a)) );
            return resultado;
        }
    }
    public Complejo aLa (int a){
        if(a == 0) return new Complejo(1);
        
        Complejo inicial = new Complejo(this);
        Complejo resultado = new Complejo(real,imaginario);
        
        for(int i = 1; i < a; i++) resultado.multiplicar(inicial);
        
        if(a > 0) return resultado;
        else return resultado.inverso();

    }
    public Complejo inverso(){
        Complejo resultado = new Complejo(real/(real*real + imaginario*imaginario), - imaginario/(real*real + imaginario*imaginario));
        return resultado;
    }
    public Colorcirijillo aPixel(Color[] paleta, int[] alphas){
        
        double r, ang;
        r = Math.sqrt(real * real + imaginario * imaginario);
        double cos = real/r;
        ang = (imaginario > 0) ? Math.acos(cos) : 2*Math.PI - Math.acos(cos);
        Color temp = paleta[ (int)((ang*(paleta.length-1))/(2*Math.PI))  ];
        int pos = (int)((((Math.pow(Math.E, 8)*Math.log(r+1))%alphas.length)/alphas.length)*alphas.length);
        int alpha = alphas[alphas.length-1-pos];
        
        Color nuevo = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), alpha);
        if(pos>384) return new Colorcirijillo(nuevo, Color.WHITE);
        else return new Colorcirijillo(nuevo, Color.BLACK);
    }
}
