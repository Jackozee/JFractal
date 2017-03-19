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
    
    //--------------------------propiedades dinámicas---------------------------
    
    public double modulo() { return Math.sqrt(real * real + imaginario * imaginario); }
    public double anguloPositivo(){
        double cos = real/modulo(); //cateto adyacente entre hipotenusa
        if(imaginario == 0 && real > 0) return 0;
        if(imaginario == 0 && real < 0) return Math.PI;
        return (imaginario > 0) ? Math.acos(cos) : 2*Math.PI - Math.acos(cos);
    }
    public double anguloNeutro(){
        double cos = real/modulo(); //cateto adyacente entre hipotenusa
        if(imaginario == 0 && real > 0) return 0;
        if(imaginario == 0 && real < 0) return - Math.PI;
        return (imaginario > 0) ? Math.acos(cos) : - Math.acos(cos);
    }
    
    //---------------------------funciones básicas------------------------------
    public Complejo mas(double a) { return mas(new Complejo(a)); }
    public Complejo mas(Complejo a) { return new Complejo(real + a.real, imaginario + a.imaginario); }
    public Complejo menos(double a) { return menos(new Complejo(a)); }
    public Complejo menos(Complejo a) { return new Complejo(real - a.real, imaginario - a.imaginario); }
    public Complejo por(double a) { return por(new Complejo(a)); }
    public Complejo por(Complejo a) { return new Complejo(real * a.real - imaginario * a.imaginario, real * a.imaginario + imaginario * a.real); }
    public Complejo entre(double a) { return entre(new Complejo(a)); }
    public Complejo entre(Complejo a) { return this.por(a.inverso()); }
    
    public Complejo Exp() { return new Complejo( Math.pow(Math.E, real) * Math.cos(imaginario) , Math.pow(Math.E, real) * Math.sin(imaginario) ); }
    
    public Complejo sen() { return ( this.por(new Complejo(0,1)).Exp().menos(this.por(new Complejo(0,-1)).Exp()) ).entre(new Complejo(0,2)); }
    public Complejo cos() { return ( this.por(new Complejo(0,1)).Exp().mas(this.por(new Complejo(0,-1)).Exp()) ).entre(2); }
    public Complejo tan() { return sen().entre(cos()); }
    public Complejo cosh() { return ( this.Exp().mas(this.por(-1).Exp()) ).entre(2); }
    public Complejo senh() { return ( this.Exp().menos(this.por(-1).Exp()) ).entre(2); }
    public Complejo tanh() { return ( senh().entre(cosh())); }
    
    public Complejo csc() { return sen().inverso(); }
    public Complejo sec() { return cos().inverso(); }
    public Complejo cot() { return cos().entre(sen()); }
    public Complejo sech() { return cosh().inverso(); }
    public Complejo csch() { return senh().inverso(); }
    public Complejo coth() { return cosh().entre(senh()); }
    
    public Complejo arcsen() { return arcsen(0,0); }
    public Complejo arcsen(double ramaRaiz, double ramaLog){
        return (this.por(new Complejo(0,1)).mas( new Complejo(1).menos(this.aLa(2)).aLa(new Complejo(.5), ramaRaiz) )).log(ramaLog).por(new Complejo(0,-1));
    }
    public Complejo arccos() { return arccos(0,0); }
    public Complejo arccos(double ramaRaiz, double ramaLog){
        return (this.mas( this.aLa(2).menos(1).aLa(new Complejo(.5), ramaRaiz) )).log(ramaLog).por(new Complejo(0,-1));
    }
    public Complejo arctan() { return arctan(0); }
    public Complejo arctan(double ramaLog){
        return new Complejo(1).mas(this.por(new Complejo(0,1))).entre(new Complejo(1).menos(this.por(new Complejo(0,1)))).log(ramaLog).por(new Complejo(0,-0.5));
    }
    public Complejo arccsc() { return arccsc(0,0); }
    public Complejo arccsc(double ramaRaiz, double ramaLog){
        return this.inverso().arcsen(ramaRaiz, ramaLog);
    }
    public Complejo arcsec() { return arcsec(0,0); }
    public Complejo arcsec(double ramaRaiz, double ramaLog){
        return this.inverso().arccos(ramaRaiz, ramaLog);
    }
    public Complejo arccot() { return arccot(0); }
    public Complejo arccot(double ramaLog){
        return this.inverso().arctan(ramaLog);
    }
    
    public Complejo expReal(Double a) {
        if(a == 0) return new Complejo();
        return new Complejo( Math.pow(Math.E, real * Math.log(a)) * Math.cos(imaginario * Math.log(a)) , Math.pow(Math.E, real * Math.log(a)) * Math.sin(imaginario * Math.log(a)) );
    }
    
    public Complejo log(){ return new Complejo(Math.log(modulo()), anguloNeutro()); }
    public Complejo log(int rama){ return new Complejo(Math.log(modulo()), anguloNeutro() + 2 * Math.PI * rama); }
    public Complejo log(Double rama){
        Double min = rama*Math.PI*2 - Math.PI ;
        Double max = min + Math.PI*2;
        Double ang = anguloNeutro();
        while(ang <= min) ang += Math.PI*2;
        while(ang > max) ang -= Math.PI*2;
        return new Complejo(Math.log(modulo()), ang);
    }
    
    public Complejo aLa(int potencia) { return aLa(new Complejo(potencia)); }
    public Complejo aLa(Complejo z) { return z.por(log()).Exp(); }
    public Complejo aLa(Complejo z, double rama){ return z.por(this.log(rama)).Exp(); }
    
    public Complejo inverso() { return new Complejo(real/(real*real + imaginario*imaginario), - imaginario/(real*real + imaginario*imaginario)); }
    //----------------------funciones de extracción-----------------------------
    
    public Complejo real(){ return new Complejo(real); }
    public Complejo imaginario(){ return new Complejo(0,imaginario); }
    
    //-------------------------función para pintar------------------------------
    public Color colorFondo(int[] alphas){
        int pos = (int)((((Math.pow(Math.E, 8)*Math.log(modulo()+1))%alphas.length)/alphas.length)*alphas.length);
        if(pos>384) return Color.WHITE;
        else return Color.BLACK;
    }
    public Color colorCurvas(Color[] paleta, int[] alphas){
        
        Color temp = paleta[ (int)((anguloPositivo()*(paleta.length-1))/(2*Math.PI))  ];
        int pos = (int)((((Math.pow(Math.E, 8)*Math.log(modulo()+1))%alphas.length)/alphas.length)*alphas.length);
        int alpha = alphas[alphas.length - 1 - pos];
        
        return new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), alpha);
    }
    public Color colorHSL() {
        double saturacion = 1;
        double iluminacion = Math.pow(2, -modulo());
        double angulo = anguloPositivo();
        if(angulo < 0) angulo += Math.PI * 2;
        double C = (1 - Math.abs(2 * iluminacion - 1)) * saturacion;
        double X = C * (1 - Math.abs((angulo * 3 / Math.PI) % 2 - 1));
        double m = iluminacion - C / 2;
        Color sinBrillo = new Color(
            (float)(
                angulo < Math.PI / 3 || angulo >= Math.PI * 5 / 3 ? C :
                angulo < Math.PI * 2 / 3 || angulo >= Math.PI * 4 / 3 ? X : 0
            ),
            (float)(
                angulo >= Math.PI / 3 && angulo < Math.PI ? C :
                angulo >= Math.PI * 4 / 3 ? 0 : X
            ),
            (float)(
                angulo >= Math.PI && angulo < Math.PI * 5 / 3 ? C :
                angulo < Math.PI * 2 / 3 ? 0 : X
            )
        );
        
        return new Color(
            (int)(sinBrillo.getRed() + m * 255),
            (int)(sinBrillo.getGreen() + m * 255),
            (int)(sinBrillo.getBlue() + m * 255)
        );
    }
}
