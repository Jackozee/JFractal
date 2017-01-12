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

import java.awt.*;

/**
 *
 * @author SamuelCoral
 */
public class DibujarMandelbrot extends Thread {
    
    public static Color colorMandelbrot(Complejo valor, long iteraciones, Color[] paleta) {
        
        Complejo inicial = new Complejo(valor);
        
        long c;
        for(c = 0; c < iteraciones; c++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            valor.multiplicar(valor);
            valor.sumar(inicial);
            
            if(Math.sqrt(valor.real * valor.real + valor.imaginario * valor.imaginario) > 2) return paleta[(int)(c % paleta.length)];
        }
        
        return Color.BLACK;
    }
    
    private final Graphics2D graficos;
    private final Dimension areaPintar;
    private final double x1, y1;
    private final long pixelesXUnidad, iteraciones;
    private final Color[] paleta;
    
    public DibujarMandelbrot(Graphics2D graficos, Dimension areaPintar, double x1, double y1, long pixelesXUnidad, long iteraciones, Color[] paleta) {
        
        this.graficos = graficos;
        this.areaPintar = areaPintar;
        this.x1 = x1;
        this.y1 = y1;
        this.pixelesXUnidad = pixelesXUnidad;
        this.iteraciones = iteraciones;
        this.paleta = paleta;
    }
    
    @Override
    public void run() {
        
        int x, y;
        for(y = 0; y < areaPintar.getHeight(); y++) for(x = 0; x < areaPintar.getWidth(); x++) {
            
            if(Thread.currentThread().isInterrupted()) break;
            graficos.setColor(colorMandelbrot(new Complejo(x1 + (double)x / pixelesXUnidad, y1 + (double)y / pixelesXUnidad), iteraciones, paleta));
            graficos.drawLine(x, y, x, y);
        }
    }
}
