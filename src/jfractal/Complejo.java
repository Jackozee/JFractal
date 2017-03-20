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
    
    public Complejo inverso() { return new Complejo(real/(real*real + imaginario*imaginario), - imaginario/(real*real + imaginario*imaginario)); }
    
    public Complejo mas(double a) { return mas(new Complejo(a)); }
    public Complejo mas(Complejo a) { return new Complejo(real + a.real, imaginario + a.imaginario); }
    public Complejo menos(double a) { return menos(new Complejo(a)); }
    public Complejo menos(Complejo a) { return new Complejo(real - a.real, imaginario - a.imaginario); }
    public Complejo por(double a) { return por(new Complejo(a)); }
    public Complejo por(Complejo a) { return new Complejo(real * a.real - imaginario * a.imaginario, real * a.imaginario + imaginario * a.real); }
    public Complejo entre(double a) { return entre(new Complejo(a)); }
    public Complejo entre(Complejo a) { return this.por(a.inverso()); }
    
    public Complejo log(){ return new Complejo(Math.log(modulo()), anguloNeutro()); }
    public Complejo log(int rama){ return new Complejo(Math.log(modulo()), anguloNeutro() + 2 * Math.PI * rama); }
    public Complejo log(Double rama){
        Double min = Math.PI*(rama*2 - 1);
        Double max = Math.PI*(rama*2 + 1);
        Double ang = anguloNeutro();
        while(ang <= min) ang += Math.PI*2;
        while(ang > max) ang -= Math.PI*2;
        return new Complejo(Math.log(modulo()), ang);
    }
    
    public Complejo aLa(double potencia) { return aLa(new Complejo(potencia)); }
    public Complejo aLa(Complejo z) { return z.por(log()).Exp(); }
    public Complejo aLa(Complejo z, double rama){ return z.por(this.log(rama)).Exp(); }
    
    public Complejo Exp() { return new Complejo( Math.pow(Math.E, real) * Math.cos(imaginario) , Math.pow(Math.E, real) * Math.sin(imaginario) ); }
    public Complejo Exp(double base) { return Exp(new Complejo(base)); }
    public Complejo Exp(double base, double rama) { return Exp(new Complejo(base), rama); }
    public Complejo Exp(Complejo base) { return base.aLa(this); }
    public Complejo Exp(Complejo base, double rama) { return base.aLa(this, rama); }
    
    //---------------funciones trigonométricas e hiperbólicas-------------------
    
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
    
    //------------------inversos de funciones trigonométricas-------------------
    
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
    
    //------------------inversos de funciones trigonométricas-------------------
    
    public Complejo arcsenh() { return arcsenh(0,0); }
    public Complejo arcsenh(double ramaRaiz, double ramaLog){
        return this.aLa(2).mas(1).aLa(new Complejo(0.5), ramaRaiz).mas(this).log(ramaLog);
    }
    public Complejo arccosh() { return arccosh(0,0); }
    public Complejo arccosh(double ramaRaiz, double ramaLog){
        return this.aLa(2).menos(1).aLa(new Complejo(0.5), ramaRaiz).mas(this).log(ramaLog);
    }
    public Complejo arctanh() { return arctanh(0); }
    public Complejo arctanh(double ramaLog){
        return new Complejo(1).mas(this).entre(new Complejo(1).menos(this)).log(ramaLog).por(0.5);
    }
    public Complejo arccsch() { return arccsch(0,0); }
    public Complejo arccsch(double ramaRaiz, double ramaLog){
        return this.inverso().arcsenh(ramaRaiz, ramaLog);
    }
    public Complejo arcsech() { return arcsech(0,0); }
    public Complejo arcsech(double ramaRaiz, double ramaLog){
        return this.inverso().arccosh(ramaRaiz, ramaLog);
    }
    public Complejo arccoth() { return arccoth(0); }
    public Complejo arccoth(double ramaLog){
        return this.inverso().arctanh(ramaLog);
    }
    
    //----------------------funciones de extracción-----------------------------
    
    public Complejo real(){ return new Complejo(real); }
    public Complejo imaginario(){ return new Complejo(0,imaginario); }
    
    //------------------------funciones para pintar-----------------------------
    public Color colorCurvas(Color[] paleta, int[] alphas){
        
        int pos = (int)((((Math.pow(Math.E, 8)*Math.log(modulo()+1))%alphas.length)/alphas.length)*alphas.length);
        Color fondo = (pos > 384) ? Color.WHITE : Color.BLACK;
        Color arg = paleta[ (int)((anguloPositivo()*(paleta.length-1))/(2*Math.PI))  ];
        
        double argR = arg.getRed() / 255d;
        double argG = arg.getGreen() / 255d;
        double argB = arg.getBlue() / 255d;
        double argA = alphas[alphas.length - 1 - pos] / 255d;
        
        double fondoR = fondo.getRed() / 255d;
        double fondoG = fondo.getGreen() / 255d;
        double fondoB = fondo.getBlue() / 255d;
        
        double TargetR = ((1d - argA) * fondoR) + (argA * argR);
        double TargetG = ((1d - argA) * fondoG) + (argA * argG);
        double TargetB = ((1d - argA) * fondoB) + (argA * argB);
        
        return new Color(
            (int)(TargetR * 255),
            (int)(TargetG * 255),
            (int)(TargetB * 255)
        );
    }
    public Color colorHSL(){ return colorHSL(false); }
    public Color colorHSL(boolean switched) {
        double saturacion = 1;
        double iluminacion = switched ? 1 - Math.pow(2, -modulo()) : Math.pow(2, -modulo());
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
