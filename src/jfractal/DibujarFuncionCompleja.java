/*
 * Copyright (C) 2017 Jackozee
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

import java.awt.*;

/**
 *
 * @author Jackozee
 */
public class DibujarFuncionCompleja extends Thread {
    
    private final Graphics2D graficos;
    private final Dimension areaPintar;
    private final double x1, y1;
    private final long pixelesXUnidad, iteraciones;
    private final Color[] paleta;
    private final int[] alphas;
    
    public DibujarFuncionCompleja(Graphics2D graficos, Dimension areaPintar, double x1, double y1, long pixelesXUnidad, long iteraciones, Color[] paleta, int[] alphas) {
        
        this.graficos = graficos;
        this.areaPintar = areaPintar;
        this.x1 = x1;
        this.y1 = y1;
        this.pixelesXUnidad = pixelesXUnidad;
        this.iteraciones = iteraciones;
        this.paleta = paleta;
        this.alphas = alphas;
    }
    
    public static long CoeficienteBinomial(long t, long r){
        
        if(t==r || r==0) return 1;
        if(r==1 || r==t-1) return t;
        
        long a,c,d;
        for(c = a = 1; c < r; a *= ++c);
        for(c = d = t; c > t-r+1; d *= --c);
        
        return (d/a);
    }
    
    public static Colorcirijillo PotenciasDeZ(Complejo valor, long iteraciones, Color[] paleta, int[] alphas) {
        
        //Cambie el parámetro del método "aLaEntero(n)" para obtener las potencias enteras de z.
        
        Complejo fin = valor.aLa(-1);
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo SenZ(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo inicial = z.inverso();
        Complejo termino;
        Complejo fin = new Complejo();
        
        long factorial,t,signo=1;
        
        long c;
        for(c = 1; c < iteraciones*2; c=c+2) {
            
            if(Thread.currentThread().isInterrupted()) break;
            
            for(t = factorial = 1; t < c; factorial *= ++t);
            termino = inicial.aLa((int)c).entre(new Complejo(factorial)).por(new Complejo(signo));
            //for(t = 1 ; t < c ; t++) paso.multiplicar(inicial); eliminar comentario para generar una funcion to'a loca
            fin.sumar(termino);
            signo = (signo==1) ? -1 : 1;
        }
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo CosZ(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo paso;
        Complejo fin = new Complejo();
        
        long factorial,t,signo=1;
        
        long c;
        for(c = 0; c <= iteraciones*2; c=c+2) {
            
            if(Thread.currentThread().isInterrupted()) break;
            
            for(t = factorial = 1; t < c; factorial *= ++t);
            paso = inicial.aLa((int)c).entre(new Complejo(factorial)).por(new Complejo(signo));
            fin.sumar(paso);
            signo = (signo==1) ? -1 : 1;
        }
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo PotenciaDeE(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas){
        
        Complejo fin;
        fin = inicial.elevandoBaseReal(Math.E);
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionZeta(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo termino;
        Complejo fin = new Complejo();
        
        long c;
        for(c = 1; c <= iteraciones; c++) {
            if(Thread.currentThread().isInterrupted()) break;
            
            termino = inicial.elevandoBaseReal((double)c).inverso();
            fin.sumar(termino);
        }
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionZetaRiemann(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo factor;
        factor = new Complejo(1).entre(new Complejo(1).menos(new Complejo(1).menos(inicial).elevandoBaseReal((double)2)));
        
        long n;
        long k;
        double f;
        Complejo sum_ex = new Complejo();
        
        for(n = 0; n <= iteraciones; n++) {
            f = 1.0/(1 << (n+1));
            Complejo sum_in = new Complejo();
            
            for(k = 0; k <= n; k++){
                
                Complejo interior = new Complejo();
                sum_in.sumar(interior.menos(inicial).elevandoBaseReal((double)k+1).por(new Complejo(Math.pow(-1,k) * CoeficienteBinomial(n,k))));
            }
            sum_ex.sumar(sum_in.por(new Complejo(f)));
        }
        Complejo fin = sum_ex.por(factor);
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionMafufa1(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo fin;
        fin = z.aLa(2).menos(new Complejo(1)).por( z.menos(new Complejo(2,1)).aLa(2) ).entre( z.aLa(2).mas(new Complejo(2,2)) );
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionMafufa2(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo fin;
        fin = z.aLa(2).menos(new Complejo(1)).por( z.menos(new Complejo(2,1)).aLa(2) );
        fin = fin.entre( z.aLa(2).mas(new Complejo(2,2)) );
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionMafufa3(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo fin;
        /*
        fin = z.aLa(2).mas(new Complejo(2,2));
        fin = fin.por( z.aLa(2).menos(new Complejo(1)).por(z.menos(new Complejo(2,1))).mas( z.menos(new Complejo(2,1).aLa(2).por(z)) ) );
        fin = fin.menos( z.menos(new Complejo(2,1)).aLa(2).por( z.aLa(2).menos(new Complejo(1)) ).por(z) );
        fin = fin.entre( z.aLa(2).mas(new Complejo(2,2)).aLa(2) );
        fin = fin.por(new Complejo(2));
        */
        
        fin = z.aLa(2).mas(new Complejo(2,2));
        fin = fin.por( z.aLa(3).mas( z.aLa(2).por(new Complejo(-2,-1)) ).menos(z).mas(new Complejo(2,1)) );
        fin = fin.menos( z.menos(new Complejo(2,1)).aLa(2).por( z.aLa(3).menos(z) ) );
        fin = fin.entre( z.aLa(4).mas( z.aLa(2).por(new Complejo(4,4)) ).mas(new Complejo(0,8)) );
        fin = fin.por(new Complejo(2));
        
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionMafufa4(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *   (z^2-i)    num = numerador
         * -----------
         * 2(z^2 + 2i)  den = denominador
         */
        
        Complejo num, den, fin;
        num = z.aLa(2).menos(new Complejo(0,1));
        den = new Complejo(2).por( z.aLa(2).mas(new Complejo(0,1)) );
        fin = num.entre(den);
        return fin.aPixel(paleta, alphas);
    }
    
    public static Colorcirijillo FuncionMafufa5(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *      2iz           num = numerador
         * --------------
         *  (z^2 + i)^2       den = denominador
         */
        
        Complejo fin, num, den;
        
        num = z.por(new Complejo(0,2));
        den = z.aLa(2).mas(new Complejo(0,1)).aLa(2);
        fin = num.entre(den);
        
        fin = fin.menos(new Complejo(0.5));
        return fin.aPixel(paleta, alphas);
    }
    
    @Override
    public void run() {
        
        int x, y;
        for(y = 0; y < areaPintar.getHeight(); y++) for(x = 0; x < areaPintar.getWidth(); x++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            
            Complejo valor = new Complejo(x1 + (double)x / pixelesXUnidad, y1 - (double)y / pixelesXUnidad);
            
            Colorcirijillo wea = PotenciaDeE(valor, iteraciones, paleta, alphas);
            
            graficos.setColor(wea.fondo);
            graficos.drawLine(x, y, x, y);
            
            graficos.setColor(wea.nuevo);
            graficos.drawLine(x, y, x, y);
        }
    }
}
