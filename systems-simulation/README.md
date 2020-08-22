# SimulacionDeSistemas

## Instrucciones de compilacion y ejecucion

Luego de clonar el repositorio:

```sh
cd src
mvn clean package                               # build
java -jar systems-simulation-1.0-SNAPSHOT.jar   # run
```

## Input

### Estatico

El input estatico tiene el siguiente formato:

```txt
t
N
L
M
Rc
B
Pr11, Pr12, Pr13
Pr21, Pr22, Pr23
...
PrN1, PrN2, PrN3
```

Donde:
- t: Cantidad de snapshots temporales que se incluiran los archivos de input dinamico. Por ejemplo si t = 5, el primer archivo se llamara 0.txt y contendra los tiempos 0-4, el segundo archivo se llamara 5.txt y contendra 5-9, y asi sucesivamente.
- N: Cantidad de particulas que tendra el sistema
- L: Dimension del espacio (longitud de un lado)
- M: Para Cell Index Method, cantidad de celdas por lado en las que se dividira el espacio (Por ejemplo si M = 2, se dividira el espacio en 4 celdas, 2 a lo alto y 2 a lo largo).
- Rc: Radio de interaccion de particulas
- B: Determina si usar Contorno Periodico. ( 1 = Contorno periodico. 0 = Contorno cerrado).
- PrXY: Propiedad Y de la particula con id X

### Dinamico

Aca se muestra el input dinamico, en el ejemplo solo hay 3 snapshots temporales pero dependera de la configuracion estatica

```txt
t0
x1 y1 vx1 vy1
x2 y2 vx2 vy2
x3 y3 vx3 vy3
x4 y4 vx4 vy4
...
xN yN vxN vyN
t1
x1 y1 vx1 vy1
x2 y2 vx2 vy2
x3 y3 vx3 vy3
x4 y4 vx4 vy4
...
xN yN vxN vyN
t2
x1 y1 vx1 vy1
x2 y2 vx2 vy2
x3 y3 vx3 vy3
x4 y4 vx4 vy4
...
xN yN vxN vyN
```

Donde xI e yI son las posiciones x e y de la particula I, y vxI e vyI son las velocidades de las particula I.
