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
import java.awt.image.BufferedImage;
import java.math.BigInteger;

/**
 *
 * @author Jackozee
 */
public class DibujarFuncionCompleja extends Thread {
    
    private final Graphics2D graficos;
    private final Dimension areaPintar;
    private final double x1, y1;
    private final long pixelesXUnidad, iteraciones, r[];
    private final Color[] paleta;
    private final int[] alphas;
    private static Complejo i = new Complejo(0,1);
    private final boolean col, conmutado;
    
    public DibujarFuncionCompleja(  Graphics2D graficos,
                                    Dimension areaPintar,
                                    double x1, double y1,
                                    long pixelesXUnidad,
                                    long iteraciones,
                                    long r[],
                                    Color[] paleta,
                                    int[] alphas,
                                    boolean col,
                                    boolean conmutado) {
        
        this.graficos = graficos;
        this.areaPintar = areaPintar;
        this.x1 = x1;
        this.y1 = y1;
        this.pixelesXUnidad = pixelesXUnidad;
        this.iteraciones = iteraciones;
        this.paleta = paleta;
        this.alphas = alphas;
        this.r = r;
        this.col = col;
        this.conmutado = conmutado;
    }
    
    static long binomial(final long N, final long K) {
        BigInteger ret = BigInteger.ONE;
        for (int k = 0; k < K; k++) {
            ret = ret.multiply(BigInteger.valueOf(N-k))
                     .divide(BigInteger.valueOf(k+1));
    }
    return ret.intValue();
}
    public static long CoeficienteBinomial(long t, long r){
        
        if(t==r || r==0) return 1;
        if(r==1 || r==t-1) return t;
        
        long a,c,d;
        for(c = a = 1; c < r; a *= ++c);
        for(c = d = t; c > t-r+1; d *= --c);
        
        return (d/a);
    }
    
    public static Complejo FuncionZeta(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo termino;
        Complejo fin = new Complejo();
        
        long c;
        for(c = 1; c <= iteraciones; c++) {
            if(Thread.currentThread().isInterrupted()) break;
            
            termino = inicial.Exp((double)c).inverso();
            fin = fin.mas(termino);
        }
        
        return fin;
    }
    
    public static Complejo FuncionZetaRiemann(Complejo inicial, long iteraciones, Color[] paleta, int[] alphas) {
        
        Complejo factor;
        factor = new Complejo(1).entre(new Complejo(1).menos(new Complejo(1).menos(inicial).Exp((double)2)));
        
        long n;
        long k;
        double f;
        Complejo sum_ex = new Complejo();
        
        for(n = 0; n <= iteraciones; n++) {
            f = 1.0/(1 << (n+1));
            Complejo sum_in = new Complejo();
            
            for(k = 0; k <= n; k++){
                
                Complejo interior = new Complejo();
                sum_in = sum_in.mas(interior.menos(inicial).Exp((double)k+1).por(new Complejo(Math.pow(-1,k) * CoeficienteBinomial(n,k))));
            }
            sum_ex = sum_ex.mas(sum_in.por(new Complejo(f)));
        }
        Complejo fin = sum_ex.por(factor);
        
        return fin;
    }
    
    public static Complejo FuncionMafufa1(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        /*
             (z^2 - 1)(z - 2 - i)^2
            ------------------------
                (z^2 + 2 + 2i)
                    
        */
        
        Complejo fin;
        fin = z.aLa(2).menos(1).por( z.menos(new Complejo(2,1)).aLa(2) ).entre( z.aLa(2).mas(new Complejo(2,2)) );
        
        return fin;
    }
    
    public static Complejo FuncionMafufa1Derivada(Complejo z, long iteraciones, Color[] paleta, int[] alphas) {
        
        /*
            [z^4 + (4+4i)z^2 - (4+7i)z - (2+2i)] 2[z - (2+i)]
             ------------------------
                (z^2 + 2 + 2i)^2
            
            [a+b-c]d
            --------
               e
        */
        
        Complejo a = z.aLa(4);
        Complejo b = new Complejo(4,4).por(z.aLa(2));
        Complejo c = new Complejo(4,7).por(z).mas(new Complejo(2,2));
        Complejo d = z.menos(new Complejo(2,1)).por(2);
        Complejo e = z.aLa(2).mas(new Complejo(2,2)).aLa(2);
        
        return a.mas(b).menos(c).por(d).entre(e);
    }
    
    public static Complejo FuncionMafufa2(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /*
         *   (z^2 - i)   num = numerador
         * -----------
         * 2(z^2 + i)  den = denominador
         */
        
        Complejo num, den;
        num = z.aLa(2).menos(i);
        den = z.aLa(2).mas(i).por(2);
        return num.entre(den);
    }
    
    public static Complejo FuncionMafufa2Derivada(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *      2iz           num = numerador
         * --------------
         *  (z^2 + i)^2       den = denominador
         */
        
        Complejo num, den;
        num = z.por(i).por(2);
        den = z.aLa(2).mas(i).aLa(2);
        return num.entre(den);
    }
    
    public static Complejo FuncionMafufa2Derivada2(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *  - 6iz^2 - 2   num = numerador
         * ------------- 
         *  (z^2 + i)^3   den = denominador
         */
        
        Complejo num, den;
        
        num = z.aLa(2).por(new Complejo(0,-6)).menos(2);
        den = z.aLa(2).mas(i).aLa(3);
        return num.entre(den);
        
    }
    
    public static Complejo FuncionMafufa3(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *      2iz           num = numerador
         * ------------- -0.5
         *  (z^2 + i)^2       den = denominador
         */
        
        Complejo num, den;
        num = z.por(i).por(2);
        den = z.aLa(2).mas(i).aLa(2);
        return num.entre(den).menos(0.5);
    }
    
    
    public static Complejo FuncionMafufa4(Complejo z, long iteraciones, Color[] paleta, int[] alphas){
        
        /* dibuja la función:
         *   z+1    num = numerador
         * -------- 
         *   z-1    den = denominador
         */
        
        Complejo num, den;
        
        num = z.mas(1);
        den = z.menos(1);
        return num.entre(den);
    }
    @Override
    public void run() {
        
        int x, y;
        for(y = 0; y < areaPintar.getHeight(); y++) for(x = 0; x < areaPintar.getWidth(); x++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            
            Complejo valor = new Complejo(x1 + (double)x / pixelesXUnidad, y1 - (double)y / pixelesXUnidad);
            
            Complejo func = valor.log(r[0]/100d);
            
            if(!col) graficos.setColor(func.colorHSL(conmutado));
            else graficos.setColor(func.colorCurvas(paleta,alphas));
            
            graficos.drawLine(x, y, x, y);
        }
    }
}
