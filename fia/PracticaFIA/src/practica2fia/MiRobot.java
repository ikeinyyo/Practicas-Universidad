/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica2fia;

import java.util.ArrayList;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;


/**
 *
 * @author mireia
 */
public class MiRobot extends Agent{

        //Variables utilizadas para el controlador difuso
        RangeSensorBelt sonars;
        FuzzyController controller;
        
        //Variables generales
        int mundo[][]; //Datos del entorno
        int origen; //Punto de partida del robot. Será la columna 1 y esta fila
        int destino; //Punto de destino del robot. Será la columna tamaño-1 y esta fila
        char camino[][]; //Camino que debe seguir el robot. Será el resultado del A*
        int expandidos[][]; //Orden de los nodos expandidos. Será el resultado del A*
        int tamaño; //Tamaño del mundo


        public MiRobot(Vector3d position, String name, Practica1 practica1) {
            super(position, name);

            //Prepara las variables
            tamaño = practica1.tamaño_mundo;

            mundo = new int[tamaño][tamaño];
            camino = new char[tamaño][tamaño];
            expandidos = new int[tamaño][tamaño];
            origen = practica1.origen;
            destino = practica1.destino;
            mundo = practica1.mundo;
            
            //Inicializa las variables camino y expandidos donde el A* debe incluir el resultado
            for(int i=0;i<tamaño;i++)
                for(int j=0;j<tamaño;j++){
                    camino[i][j] = '.';
                    expandidos[i][j] = -1;
                }

            // Añade sonars
            sonars = RobotFactory.addSonarBeltSensor(this); // de 0 a 1.5m
        }

        //Calcula el A*
        public int AEstrella(){
            
            //fila/columna
            
            int result = 0;
            int exp = 0;

            ArrayList<Nodo> listaInterior = new ArrayList<Nodo>();
            ArrayList<Nodo> listaFrontera = new ArrayList<Nodo>();
            
            Position fin = new Position(destino, tamaño - 2);
            
            Nodo raiz = new Nodo(new Position(origen, 1));
            raiz.calcularH(fin);
            listaFrontera.add(raiz);
            
            while(!listaFrontera.isEmpty())
            {
                //Traza:
                //System.out.println("Traza: ");
                /*System.out.print("Lista Interior: ");
                mostrarLista(listaInterior);
                System.out.print("Lista Frontera: ");
                mostrarLista(listaFrontera);*/
                //FinTraza
                Nodo n = getMenorF(listaFrontera);
                
                //Trza
                //System.out.println("Nodo seleccionado: " + n);
                //Fin Traza
                //Escribir en expandidos.
                Position pos = n.getPosition();
                expandidos[pos.getX()][pos.getY()] = exp;
                exp++;
                
                
                listaFrontera.remove(n);
                listaInterior.add(n);
                
                if(n.isMeta(destino, tamaño-2))
                {
                    //Fin, reconstruir camino
                    crearCamino(n);
                    mostrarCamino();
                    mostrarExpandidos();
                    
                    
                    
                    return result;
                }
                
                ArrayList<Nodo> hijos = getHijos(n);
                
                for(Nodo m: hijos)
                {
                    int g;
                    if(!isInArrayList(listaInterior, m))
                    {
                        g = n.getG() + 1;
                        
                        if(!isInArrayList(listaFrontera, m))
                        {
                            m.setG(g);
                            m.setPadre(n);
                            m.calcularH(fin);
                            listaFrontera.add(m);
                        }
                        else if(g < m.getG())
                        {
                            m.setG(g);
                            m.calcularH(fin);
                            m.setPadre(n);
                        }
                              
                    }
                }
                
                
            }
            
            System.out.println("No se ha encontrado solución");
            result = 1;
            return result;
        }
        
        private void mostrarLista(ArrayList<Nodo> nodos)
        {
            for(Nodo n : nodos)
            {
                System.out.print(n + " ");
            }
            System.out.println();
        }
        public Nodo getMenorF(ArrayList<Nodo> nodos)
        {
            Nodo nodo = null;
            
            for(Nodo n: nodos)
            {
                if(nodo == null || n.getF() < nodo.getF())
                {
                    nodo = n;
                }
            }
            
            return nodo;
        }
        
        public boolean isInArrayList(ArrayList<Nodo> nodos, Nodo nodo)
        {
            boolean existe = false;
            
            for(Nodo n: nodos)
            {
                if(Position.equal(n.getPosition(), nodo.getPosition()))
                {
                    existe = true;
                    break;
                }
            }
            
            return existe;
        }

        public ArrayList<Nodo> getHijos(Nodo nodo)
        {
            ArrayList<Nodo> hijos = new ArrayList<Nodo>();
            
            Position pos = nodo.getPosition();
            int x, y;
            
            x = pos.getX();
            y = pos.getY() + 1;
            if(y >= 0 && y < tamaño)
            {
                if(mundo[x][y] == 0)
                {
                    hijos.add(new Nodo(new Position(x, y)));
                }
            }
            
            x = pos.getX() - 1;
            y = pos.getY();
            if(x >= 0 && x < tamaño)
            {
                if(mundo[x][y] == 0)
                {
                    hijos.add(new Nodo(new Position(x, y)));
                }
            }
            
            
            x = pos.getX() + 1;
            y = pos.getY();
            if(x >= 0 && x < tamaño)
            {
                if(mundo[x][y] == 0)
                {
                    hijos.add(new Nodo(new Position(x, y)));
                }
            }
            
            
             x = pos.getX();
            y = pos.getY() - 1;
            if(y >= 0 && y < tamaño)
            {
                if(mundo[x][y] == 0)
                {
                    hijos.add(new Nodo(new Position(x, y)));
                }
            }
            
            return hijos;
        }
        
        private void crearCamino(Nodo n)
        {
            Nodo nodo = n;
            Position pos;
            int coste = 0;
            //Escribir en el mundo
            while(nodo != null)
            {
                pos = nodo.getPosition();
                camino[pos.getX()][pos.getY()] = 'X';
                nodo = nodo.getPadre();
                coste++;
            }
            System.out.println("Coste del camino: " + coste);
        }
        
        private void mostrarCamino()
        {
            System.out.println("Solución del A*");
            System.out.println("Camino");
            for(int i = 0; i < tamaño; i++)
            {
                for(int j = 0; j < tamaño; j++)
                {
                    System.out.print(camino[i][j] + " ");
                }

                System.out.println();
            }
        }
        
        private void mostrarExpandidos()
        {
            System.out.println("Nodos explorados");
            for(int i = 0; i < tamaño; i++)
            {
                for(int j = 0; j < tamaño; j++)
                {
                    System.out.print(expandidos[i][j] + " ");
                }

                System.out.println();
            }
        }
        //Función utilizada para la parte de lógica difusa donde se le indica el siguiente punto al que debe ir el robot.
        //Busca cual es el punto más cercano.
        public Point3d puntoMasCercano(Point3d posicion){
            int inicio;
            Point3d punto = new Point3d(posicion);
            double distancia;
            double cerca = 100;

            inicio = (int) (tamaño-(posicion.z+(tamaño/2)));
            
            for(int i=0; i<tamaño; i++)
                for(int j=inicio+1; j<tamaño; j++){
                    if(camino[i][j]=='X'){
                        distancia = Math.abs(posicion.x+(tamaño/2)-i) + Math.abs(tamaño-(posicion.z+(tamaño/2))-j);
                        if(distancia < cerca){
                            punto.x=i;
                            punto.z=j;
                            cerca = distancia;
                        }
                    }
                }

            return punto;
        }

        /** This method is called by the simulator engine on reset. */
    @Override
        public void initBehavior() {

            System.out.println("Entra en initBehavior");
            //Calcula A*
            int a = AEstrella();

            if(a!=0){
                System.err.println("Error en el A*");
            }else{
                // init controller
                controller = new FuzzyController();
            }
        }

        /** This method is call cyclically (20 times per second)  by the simulator engine. */
    @Override
        public void performBehavior() {

            double angulo;
            int giro;

            //Ponemos las lecturas de los sonares al controlador difuso
            //System.out.println("Fuzzy Controller Input:");
            float[] sonar = new float[9];
            for(int i=0; i<9; i++){
                if(sonars.getMeasurement(i)==Float.POSITIVE_INFINITY){
                    sonar[i] = sonars.getMaxRange();
                } else {
                    sonar[i] = (float) sonars.getMeasurement(i);
                }

                //System.out.println("    > S"+ i +": " + sonar[i]);
            }

     
            //Calcula ángulo del robot
            Transform3D rotTrans = new Transform3D();
            this.rotationGroup.getTransform(rotTrans); //Obtiene la transformada de rotación

            //Debe calcular el ángulo a partir de la matriz de transformación
            //Nos quedamos con la matriz 3x3 superior
            Matrix3d m1 = new Matrix3d();
            rotTrans.get(m1);

            //Calcula el ángulo sobre el eje y
            angulo = -java.lang.Math.asin(m1.getElement(2,0));

            if(angulo<0.0)
                angulo += 2*Math.PI;
            assert(angulo>=0.0 && angulo<=2*Math.PI);

            //Calcula la dirección
            if(m1.getElement(0, 0)<0)
                angulo = -angulo;
            angulo = angulo*180/Math.PI;            
            if(angulo<0 && angulo>-90)
                angulo += 180;
            if(angulo<-270 && angulo>-360)
                angulo += 180+360;


            //Calcula el siguiente punto al que debe ir del A*
            Point3d coord = new Point3d();
            this.getCoords(coord);

            Point3d punto = puntoMasCercano(coord);
            coord.x = coord.x+(tamaño/2);
            coord.z = tamaño-(coord.z+tamaño/2);
            coord.x = (int)coord.x;
            coord.z = (int)coord.z;
            
            
            //Calcula distancia y ángulo del vector, creado desde el punto que se encuentra el robot,
            //hasta el punt que se desea ir
            double distan = Math.sqrt(Math.pow(coord.z-punto.z, 2)+Math.pow(coord.x-punto.x, 2));    
            double phi= Math.atan2((punto.z-coord.z),(punto.x - coord.x));
            phi = phi*180/Math.PI;
            
            //Calcula el giro que debe realizar el robot. Este valor es el que se le pasa al controlador difuso.
            double rot = phi-angulo;
            if(rot<-180)
                rot += 360;
            if(rot>180)
                rot -=360;
            
            //System.out.println("Angulo de giro: "+rot);
            
            //Ejecuto el controlador
            controller.step(sonar, rot);

            //Obtengo las velocidades calculadas y las aplico al robot
            setTranslationalVelocity(controller.getVel());
            setRotationalVelocity(controller.getRot());

            //Para mostrar los valores del controlador
            //System.out.println("Fuzzy Controller Output:");
            //System.out.println("    >vel: "+ controller.getVel());
            //System.out.println("    >rot: "+ controller.getRot());
        }

}

class Nodo {
    
    private Position position;
    private Nodo padre;
    private int g;
    private int h;
    
    
    public Nodo(Position pos, int g_, int h_)
    {
        position = pos;
        g = g_;
        h = h_;
        padre = null;
    }
    
    public Nodo(Position pos, int g_)
    {
        position = pos;
        g = g_;
        h = 0;
        padre = null;
    }
    
    public Nodo(Position pos)
    {
        position = pos;
        h = 0;
        g = 0;
        padre = null;
    }
    
    public Nodo()
    {
        position = new Position(0, 0);
        h = 0;
        g = 0;
        padre = null;
    }
    
    public void setPosition(Position pos)
    {
        position = pos;
    }
    
   public int getF()
   {
       return g + h;
   }
   
   public boolean isMeta(int x, int y)
   {
       return (Position.equal(x, y, position));
   }
   
   public Position getPosition()
   {
       return position;
   }
   
   public int getG()
   {
       return g;
   }
   
   public int getH()
   {
       return h;
   }
   
   public void setG(int g_)
   {
       g = g_;
   }
   
   public void setH(int h_)
   {
       h = h_;
   }
   
   public void setPadre(Nodo padre_)
   {
       padre = padre_;
   }
   
   public Nodo getPadre()
   {
       return padre;
   }
   
   public String toString()
   {
       return "[" + position + " G: " + g + " H: " + h + " F: " + getF() + "]";
   }
   public void calcularH(Position destino)
   {
       //Distancia Manhattan
       h = Math.abs(destino.getX() - position.getX()) + Math.abs(destino.getY() - position.getY());
       
       //Disntancia Euclídea
       //h = (int)Math.sqrt(Math.pow(destino.getX() - position.getX(), 2) + Math.pow(destino.getY() - position.getY(), 2));
       
       //h(x) = 0
       //h = 0;
   }
}

class Position {
    
    private int px;
    private int py;
    
    public Position(int x, int y)
    {
        px = x;
        py = y;
    }
    public int getX()
    {
        return px;
    }
    public int getY()
    {
        return py;
    }
    public void setPosition(int x, int y)
    {
        px = x;
        py = y;
    }
    
    static public boolean equal(int x, int y, Position pos)
    {
        return (pos.getX() == x && pos.getY() == y);
    }
    
    static public boolean equal(Position pos1, Position pos2)
    {
        return (pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY());
    }
    
    public String toString()
    {
        return "(" + px + "," + py + ")";
    }
}
