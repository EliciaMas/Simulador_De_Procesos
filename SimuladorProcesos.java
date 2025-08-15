import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulador de Gestión de Procesos en Memoria
 * Autor: [Tu Nombre o Equipo]
 * Descripción: Este programa simula un sistema operativo que administra
 *              procesos con una RAM limitada a 1 GB.
 */
public class SimuladorProcesos {

    // Tamaño total de la memoria RAM en MB
    private static final int RAM_TOTAL = 1024;

    // Memoria actualmente disponible
    private static int memoriaDisponible = RAM_TOTAL;

    // Lista de procesos en ejecución
    private static final List<Proceso> procesosEnEjecucion = Collections.synchronizedList(new ArrayList<>());

    // Cola de espera para procesos sin memoria suficiente
    private static final Queue<Proceso> colaDeEspera = new LinkedList<>();

    // Contador para asignar PID únicos
    private static final AtomicInteger contadorPID = new AtomicInteger(1);

    // Clase interna que representa un proceso
    static class Proceso extends Thread {
        int pid;
        String nombre;
        int memoriaRequerida;
        int duracionSegundos;

        public Proceso(String nombre, int memoriaRequerida, int duracionSegundos) {
            // Asignar PID automáticamente
            this.pid = contadorPID.getAndIncrement();

            // Si no se pasa nombre, se genera uno genérico
            this.nombre = (nombre == null || nombre.isEmpty()) ? "Proceso-" + pid : nombre;

            this.memoriaRequerida = memoriaRequerida;
            this.duracionSegundos = duracionSegundos;
        }

        @Override
        public void run() {
            try {
                // Simulación de ejecución del proceso
                System.out.println("Ejecutando " + nombre + " (PID: " + pid + ") - Memoria: " + memoriaRequerida + "MB");
                Thread.sleep(duracionSegundos * 1000);

                // Cuando termina, se libera la memoria
                liberarMemoria(this);

            } catch (InterruptedException e) {
                System.out.println("El proceso " + nombre + " fue interrumpido.");
            }
        }
    }

    // Método para intentar iniciar un proceso
    public static void iniciarProceso(String nombre, int memoria, int duracion) {
        Proceso nuevoProceso = new Proceso(nombre, memoria, duracion);

        // Si hay suficiente memoria, se ejecuta
        if (memoriaDisponible >= memoria) {
            memoriaDisponible -= memoria;
            procesosEnEjecucion.add(nuevoProceso);
            nuevoProceso.start();
            mostrarEstadoMemoria();
        } else {
            // Si no hay memoria suficiente, va a la cola de espera
            colaDeEspera.add(nuevoProceso);
            System.out.println("No hay memoria suficiente. " + nuevoProceso.nombre + " se ha puesto en cola.");
            mostrarEstadoMemoria();
        }
    }

    // Método para liberar memoria y ejecutar procesos en espera si es posible
    private static synchronized void liberarMemoria(Proceso p) {
        memoriaDisponible += p.memoriaRequerida;
        procesosEnEjecucion.remove(p);
        System.out.println("Proceso finalizado: " + p.nombre + " (PID: " + p.pid + ")");
        mostrarEstadoMemoria();

        // Intentar ejecutar procesos en cola
        Iterator<Proceso> it = colaDeEspera.iterator();
        while (it.hasNext()) {
            Proceso procesoEnCola = it.next();
            if (memoriaDisponible >= procesoEnCola.memoriaRequerida) {
                memoriaDisponible -= procesoEnCola.memoriaRequerida;
                procesosEnEjecucion.add(procesoEnCola);
                procesoEnCola.start();
                it.remove();
            }
        }
    }

    // Método para mostrar el estado actual de la memoria y procesos
    private static void mostrarEstadoMemoria() {
        System.out.println("=== Estado de la Memoria ===");
        System.out.println("Memoria disponible: " + memoriaDisponible + " MB");
        System.out.println("Procesos en ejecución: " + procesosEnEjecucion.size());
        System.out.println("En cola: " + colaDeEspera.size());
        System.out.println("============================");
    }

    // Método principal
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continuar = true;

        System.out.println("=== Simulador de Gestión de Procesos en Memoria ===");

        while (continuar) {
            System.out.println("\n1. Crear proceso");
            System.out.println("2. Ver estado de memoria");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del proceso (o dejar en blanco para autogenerar): ");
                    String nombre = sc.nextLine();
                    System.out.print("Memoria requerida (MB): ");
                    int memoria = sc.nextInt();
                    System.out.print("Duración (segundos): ");
                    int duracion = sc.nextInt();
                    iniciarProceso(nombre, memoria, duracion);
                    break;

                case 2:
                    mostrarEstadoMemoria();
                    break;

                case 3:
                    continuar = false;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }

        sc.close();
    }
}

