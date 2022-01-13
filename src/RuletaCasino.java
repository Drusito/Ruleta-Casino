/*
 Augusto Málaga
 Quim Pallarés
 Joan Coll
 Pablo García
*/
import java.util.Random;
import java.util.Scanner;
//TODO:Que no se repitan las apuestas a numeros concretos
public class RuletaCasino {
    static int[][] ruleta = {
            {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36},
            {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35},
            {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34}
    };
    static int[] numerosApostados;
    //Los elementos "true" son rojos, los "false son negros, en la ruleta.
    static boolean[][] isRed = {
            {true, false, true, true, false, true, true, false, true, true, false, true},
            {false, true, false, false, true, false, false, true, false, false, true, false},
            {true, false, true, false, false, true, true, false, true, false, false, true},
    };
    static int[] numerosRojos = {3, 9, 12, 18, 21, 27, 30, 36, 5, 14, 23, 32, 1, 7, 16, 19, 25, 34};
    static int[] numerosNegros = {6, 15, 24, 33, 2, 8, 11, 17, 20, 26, 29, 35, 4, 13, 22, 31, 10, 28};
    static float[] apuestas = new float[0];
    static int min = 1;
    static int max = 36;
    static Scanner input = new Scanner(System.in);
    static final String RED = "\u001B[31m";
    static final String BLACK = "\u001B[30m";
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\033[0;32m";
    public static final String CYAN = "\033[0;36m";
    public static final String YELLOW = "\u001B[33m";
    static final String eleccionApuesta = GREEN + " 1." + YELLOW + "[1-12] " + GREEN +
            "2." + YELLOW + "[13-24] " + GREEN +
            "3." + YELLOW + "[25-32] " + GREEN +
            "\n 4." + YELLOW + "[1/3]  " + GREEN +
            "5." + YELLOW + "[2/3]   " + GREEN +
            "6." + YELLOW + "[3/3] " + GREEN +
            "\n 7." + YELLOW + "[1-18] " + GREEN +
            "8." + YELLOW + "[19-36] " + GREEN +
            "\n 9." + YELLOW + "[PAR] " + GREEN +
            "10." + YELLOW + "[IMPAR] " + GREEN +
            "\n11." + YELLOW + "[" + RED + "ROJO" + YELLOW + "] " + GREEN +
            "12." + YELLOW + "[" + BLACK + "NEGRO" + YELLOW + "] " + GREEN +
            "\n13." + YELLOW + "[Elegir uno o más números] " + RESET;

    static int opcionApuesta = -1; //Se tiene que poner como variable global porque al declararlo en el menú recursivo, se vuelve a inicializar en 0 y no conseguimos la funcionalidad buscada.
    static float dineroDisponible = 0;
    static float apuesta = 0.0f;
    static short restriccionGiros = 0;
    static boolean tieneDinero;
    static boolean jugando = true;

    /**
     *  Main
     * @param args
     */
    public static void main(String[] args) {
        RuletaCasino main = new RuletaCasino();
        main.init();
    }

    /**
     * Método inicializador
     */
    public static void init() {
        menuPrincipal();
    }

    /**
     * Se pregunta cuanto dinero se quiere introducir.
     */
    public static void introducirCapital() {
        dineroDisponible += leerFloat("Cuanto dinero quieres introducir? (€)");
        System.out.println("Tienes " + GREEN + dineroDisponible + "€" + RESET);
        stop(CYAN+"Pulsa ENTER para continuar...");
    }

    /**
     * Muestra los numeros y opciones de la ruleta
     */
    public static void imprimirRuleta() {

        int counter = 0;
        System.out.print(YELLOW + "       [1-12]  ");
        System.out.print("[13-24]  ");
        System.out.print("[25-32]" + RESET);
        System.out.println();
        System.out.println(YELLOW + "   ---------------------------------" + RESET);
        for (int i = 0; i < ruleta.length; i++) {
            counter = 0;
            int num;
            if (i == 1) {
                System.out.print(GREEN + 0 + RESET);
                System.out.print(YELLOW + " |");
            } else System.out.print(YELLOW + "  |");

            for (int j = 0; j < ruleta[i].length; j++) {
                if (isRed[i][j]) System.out.print(RED);
                else System.out.print(BLACK);
                System.out.print(ruleta[i][j] + " ");
            }
            System.out.println(YELLOW + "| [" + (i + 1) + "/3]");
        }
        System.out.println(YELLOW + "   ---------------------------------" + RESET);
        System.out.print(YELLOW + "   [1-18] [PAR][" + RED + "*" + YELLOW + "][" + BLACK + "*" + YELLOW + "][IMPAR] [19-36]" + RESET + "\n   ");

        stop(CYAN + "\nPulse ENTER ver el menu..." + RESET);
    }

    /**
     * Menu recursivo de las acciones que se pueden tomar
     */
    public static void menu() { //Método Recursivo
        int opcion = 0;
        tieneDinero = dineroDisponible > 0;

        if(tieneDinero || jugando) {
            opcion = leerInt(YELLOW + "Elije una opción:\n " + GREEN +
                            "1-" + YELLOW + " Ver Ruleta.\n " + GREEN +
                            "2-" + YELLOW + " Elegir Apuesta.\n " + GREEN +
                            "3-" + YELLOW + " Girar Ruleta." + YELLOW + GREEN + "\n " +
                            "4-" + YELLOW + " Mostrar tus numeros." + GREEN + "\n " +
                            "5-" + YELLOW + " Introducir más dinero." + GREEN +
                            "\n 6-" + YELLOW + " Menu Principal." + YELLOW
                    , 1, 5);

            switch (opcion) {
                case 1:
                    imprimirRuleta();
                    menu();
                    break;
                case 2:
                    jugando = true;
                    System.out.println(eleccionApuesta);
                    opcionApuesta = menuApuestas();
                    menu();
                    break;
                case 3:

                    System.out.println(opcionApuesta);
                    if (opcionApuesta != -1) {
                        girarRuleta(opcionApuesta);
                        jugando = false;
                    } else {
                        System.out.println(RED + "Todavía no has hecho ninguna apuesta." + RESET);
                        stop(CYAN + "Pulsa ENTER para continuar" + RESET);
                    }
                    menu();
                    break;
                case 4:
                    mostrarNumerosApostados();
                    menu();
                    break;
                case 5:
                    introducirCapital();
                    menu();
                    break;
                case 6:
                    menuPrincipal();
                    break;
            }
        }
        else{
            menuDinero();
            tieneDinero = dineroDisponible > 0;
            if(tieneDinero){
                menu();
            }
        }
    }

    /**
     * Menu para las apuestas (Se pregunta la opción al usuario
     * @return opcion (que elije el usuario)
     */
    public static int menuApuestas() {
        int opcion = 0;
        if(dineroDisponible > 0) {
            opcion = leerInt(CYAN + "Elige una opción" + RESET, 1, 13);
            elegirNumeros(opcion);
            do {
                apuesta = preguntarApuesta(opcion);
                if (apuesta > dineroDisponible)
                    System.out.println(RED + "No tienes tanto dinero, haz una apuesta razonable." + RESET);
            } while (apuesta > dineroDisponible);
            dineroDisponible -= apuesta;
            System.out.println(GREEN + "Tu apuesta se ha guardado correctamente." + RESET);
            restriccionGiros = 0;
        }
        else{
            System.out.println(RED + "No puedes realizar ninguna apuesta porque no tienes ni un céntimo."+RESET);
        }
        return opcion;
    }

    /**
     *  Enseña un número aleatorio del 1 al 36 con su color correspondiente
     *  Se muestra el beneficio y el dinero total.
     * @param opcionElegida
     */
    public static void girarRuleta(int opcionElegida) {
        if(restriccionGiros == 0) {
            float premio = 0;
            int min = 1;
            int max = 36;
            int index = -1;
            int contadorNumerosAcertados = 0;
            Random alea = new Random();
            int numGanador = alea.nextInt(max) + min;
            System.out.println("Preparando la ruleta...");
            stop(CYAN + "Pulse ENTER para girarla..." + RESET);
            System.out.print(RESET + "El número ganador es ");
            for (int i = 0; i < ruleta.length; i++) {
                for (int j = 0; j < ruleta[i].length; j++) {
                    if (numGanador == ruleta[i][j]) {
                        index = i;
                        if (isRed[index][j]) System.out.print(RED);
                        else System.out.print(BLACK);
                    }
                }
            }
            System.out.println(numGanador + RESET);
            switch (opcionElegida) {
                case 1:
                    //1-12
                    if (numGanador >= 1 && numGanador <= 12) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 2:
                    // 13-24
                    if (numGanador >= 13 && numGanador <= 24) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 3:
                    // 25-36
                    if (numGanador >= 25 && numGanador <= 36) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 4:
                    // 1/3
                    if (comprobarTercio(0, numGanador)) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 5:
                    // 2/3
                    if (comprobarTercio(1, numGanador)) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 6:
                    // 3/3
                    if (comprobarTercio(2, numGanador)) {
                        premio = apuesta * 3;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 7:
                    //1-18
                    if (numGanador >= 1 && numGanador <= 18) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    }
                    mensajePerdedor();
                    break;
                case 8:
                    //19-36
                    if (numGanador >= 19 && numGanador <= 36) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 9:
                    //PAR
                    if (esPar(numGanador)) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 10:
                    //IMPAR
                    if (!esPar(numGanador)) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 11:
                    //ROJO
                    if (isRedBool(numGanador)) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 12:
                    //NEGRO
                    if (!isRedBool(numGanador)) {
                        premio = apuesta * 2;
                        dineroDisponible += premio;
                        mensajeGanador(premio);
                    } else mensajePerdedor();
                    break;
                case 13:
                    //Numero/s concreto/s
                    for (int i = 0; i < numerosApostados.length; i++) {
                        if (numerosApostados[i] == numGanador) {
                            contadorNumerosAcertados++;
                            premio = apuestas[i] * 36;
                            dineroDisponible += premio;
                        }
                    }
                    if (contadorNumerosAcertados != 0) {
                        mensajeGanador(premio);
                    } else System.out.println(RED + "La próxima vez será..." + RESET);
                    break;
            }
            System.out.println("Tienes " + GREEN + dineroDisponible + "€ " + RESET);
            stop(CYAN+"Pulse ENTER para volver al menu. "+RESET);
        }
        else System.out.println(RED + "No puedes volver a girar la ruleta con la misma apuesta.");
        restriccionGiros++;
    }

    /**
     * Hace una parada en el programa para que el usuario pueda visualizar lo que hay en consola
     * @param message
     */
    public static void stop(String message) {
        System.out.println(message);
        input.nextLine();
    }

    /**
     * Muestra si el número pasado por parámetro es par.
     * @param n
     * @return boolean (si es par o no)
     */
    public static boolean esPar(int n) {
        if (n % 2 == 0) {
            return true;
        }
        return false;
    }

    /**
     * Dice si el numero generado por la ruleta es rojo o no.
     * @param numGanador
     * @return
     */
    public static boolean isRedBool(int numGanador) {
        for (int i = 0; i < numerosRojos.length; i++) {
            if (numerosRojos[i] == numGanador) return true;
        }
        return false;
    }

    /**
     * Pide un input int al usuario, si no proporciona un int se le vuelve a preguntar.
     * @param _text Texto a imprimir.
     * @param min Minimo de input
     * @param max Maximo de input
     * @return value
     */
    public static int leerInt(String _text, int min, int max) {
        int value = 0;
        Scanner read = new Scanner(System.in);
        boolean correctValue = false;
        do {
            System.out.println(CYAN + _text);
            correctValue = read.hasNextInt();
            if (correctValue) {
                value = read.nextInt();
            } else {
                System.out.println(RED + "ERROR. Input not valid.");
            }
            read.nextLine();
            if (value < min || value > max) {
                correctValue = false;
                System.out.println(RED + "Input Fuera de Rango.");
            }
        } while (!correctValue);
        return value;
    }

    /**
     * Pide un input float al usuario, si no proporciona un float se le vuelve a preguntar.
     * @param _text Texto a imprimir
     * @return value
     */
    public static float leerFloat(String _text) {
        float value = 0;
        Scanner read = new Scanner(System.in);
        boolean correctValue = false;
        do {
            System.out.println(CYAN + _text);
            correctValue = read.hasNextFloat();
            if (correctValue) {
                value = read.nextFloat();
            } else {
                System.out.println(RED + "ERROR. Input not valid.");
            }
            read.nextLine();
        } while (!correctValue);
        return value;
    }

    /**
     * Mensaje de bienvenida del programa
     */
    public static void mensajeBienvenida() {
        System.out.println(GREEN + "******* BIENVENIDO  A LA  RULETA **********" + RESET);
        imprimirRuleta();
    }

    /**
     * Muestra los números con su color correspondiente sobre los que el usuario ha apostado, si es el caso de que haya
     * apostado a números, sino enseña un mensaje diciendo que no ha apostado a ningún número.
     */
    public static void mostrarNumerosApostados() {
        if (numerosApostados != null) {
            System.out.println("TUS NUMEROS SON: ");
            for (int i = 0; i < numerosApostados.length; i++) {
                for (int j = 0; j < numerosNegros.length; j++) {
                    if(numerosApostados[i] != 0 || numerosApostados[j] != 0) {
                        if (numerosApostados[i] == numerosRojos[j]) {
                            System.out.print(RED);
                        }
                        if (numerosApostados[i] == numerosNegros[j]) {
                            System.out.print(BLACK);
                        }
                        if (j == numerosNegros.length - 1)
                            System.out.print(numerosApostados[i] + " ");
                    }
                }
            }
            System.out.println(RESET);
        } else System.out.println(RED + "No has apostado a ningún número." + RESET);
        stop(CYAN + "Pulsa ENTER para continuar..." + RESET);
    }

    /**
     * Comprueba si el número ganador está dentro del tercio elegido por el usuario.
     * @param tercio Tercio elegido por el usuario
     * @param numGanador numero generado por la ruleta
     * @return boolean (Dice si el numero está dentro del tercio o no)
     */
    public static boolean comprobarTercio(int tercio, int numGanador) {
        for (int i = 0; i < ruleta[tercio].length; i++) {
            if (numGanador == ruleta[tercio][i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pregunta por el importe que quiere apostar el usuario
     * @param opcion opcion elegida por el usuario
     * @return apuesta
     */
    public static float preguntarApuesta(int opcion)
    {
        float apuesta = 0;
        if(opcion != 13) apuesta = leerFloat("Introduce tu apuesta:");
        return apuesta;
    }

    /**
     * Elige el número o los números sobre los que quiere apostar el usuario.
     * @param opcion
     */
    public static void elegirNumeros(int opcion) {
        int numSize;
        int numeroRepetido;
        float dineroApostado = 0.0f;
        if (opcion == 13) {
            numSize = leerInt("A cuantos números quieres apostar?", 1, 36);
            numerosApostados = new int[numSize];
            apuestas = new float[numSize];
            for (int i = 0; i < numerosApostados.length; i++) {
                if(dineroDisponible > 0) {
                    numerosApostados[i] = leerInt("Selecciona un número sobre el que quieres apostar para la posición " + i + ": ", 1, 36);
                    apuestas[i] = leerFloat("Cuanto quieres apostar a este número?");
                    if (apuestas[i] > dineroDisponible) {
                        System.out.println(dineroApostado);
                        dineroApostado -= apuestas[i];

                        System.out.println(RED + "No dispones de tanto dinero. Te queda " + GREEN + (dineroDisponible - dineroApostado) + GREEN + "€ " + RED + "para apostar. Vuelve a realizar esta apuesta." + RESET);
                        i--;
                    }
                    if (dineroApostado >= dineroDisponible && i != numerosApostados.length - 1) {
                        System.out.println(RED + "No puedes apostar a más números, no te queda dinero disponible." + RESET);
                        i = numerosApostados.length - 1;
                    }
                    dineroDisponible -= apuestas[i];
                    System.out.println("Te queda " + dineroDisponible);
                }

            }
            //TODO: Opción introducir más dinero
            //comprobar numero repetidos
            for (int i = 0; i < numerosApostados.length; i++) {
                for (int j = 0; j < numerosApostados.length; j++) {
                    if (i != j && numerosApostados[i] == numerosApostados[j]) {
                        if(numerosApostados[i] != 0 && numerosApostados[j] != 0) {
                            System.out.println("Numero repetido en la posición " + j);
                            numeroRepetido = numerosApostados[i];
                            do {
                                numerosApostados[j] = leerInt(RED + "\nEl número " + numeroRepetido + " está repetido. Cambialo..." + /*CYAN+"\nCambialo...\n"*/RESET, 1, 36);
                                if (numerosApostados[j] == numeroRepetido)
                                    numerosApostados[j] = -1; //Si el número es el mismo, se le asigna el mismo número negativo para que siga el bucle hasta que se introduzca un dato correcto.
                            } while ((numerosApostados[j] < min || numerosApostados[j] > max) && numerosApostados[j] != 0);
                        }
                    }
                }
            }
            if(dineroDisponible <= 0) {
                System.out.println(GREEN + "Tu apuesta se ha guardado correctamente." + RESET);
                System.out.println(RED+"No tienes tienes dinero para realizar más apuestas. Gira la ruleta y prueba suerte"+RESET);
            }
            stop("Pulsa ENTER para continuar...");
        }
    }

    /**
     * Se muestra un mensaje de victoria junto al premio que ha obtenido el usuario.
     * @param premio
     */
    public static void mensajeGanador(float premio)
    {
        System.out.println( GREEN + "Felicidades! Has acertado el número!" + RESET +
                "\nHas ganado "+ GREEN + (premio - apuesta) + "€"+RESET);
    }

    /**
     * Se muestra un mensaje de derrota.
     */
    public static void mensajePerdedor()
    {
        System.out.println("Has perdido "+ RED + apuesta + "€"+RESET);
    }

    /**
     *  Pregunta si quiere introducir más dinero el usuario.
     */
    public static void menuDinero ()
    {
        int opcion = 0;
        opcion = leerInt(YELLOW+"1.Introducir más dinero" + "\n2.Salir" + CYAN+"\n\nElije una opción..."+RESET,1,2);
        switch (opcion){
            case 1:
                introducirCapital();
                break;
            case 2:
                break;
        }
    }

    public static void menuPrincipal () {
        int opcion=0;
        final String mensajeBienvenida = "*** Bienvenides a la ruleta ***\n";
        System.out.println(mensajeBienvenida);

        opcion = leerMenuPrincipal("Selecciona una opción:\n\t1- Empezar Partida\n\t2- Instrucciones\n\t3- Salir\n", 1, 3);

        switch (opcion) {
            case 1:
                //TODO meter menuRuleta
                mensajeBienvenida();
                introducirCapital();
                menu();
                break;

            case 2:
                menuInstruccionesPagUno();
                break;

            case 3:
                System.out.println("Saliendo del programa ...");
                break;
        }
    }
    //Control MenuPrincipal
    public static int leerMenuPrincipal(String mensaje, int min, int max) { // Aqui leera el menú

        Scanner leer = new Scanner(System.in);
        boolean vCorrecto = false;
        int valor = 0;

        do {
            System.out.print(mensaje);
            vCorrecto = leer.hasNextInt();

            if (!vCorrecto) {
                System.out.println("ERROR: Valor no apto");
            } else {
                valor = leer.nextInt();
                if (valor < min | valor > max) {
                    System.out.println("ERROR: Valor fuera de rango");
                    vCorrecto = false;
                }
            }
            leer.nextLine();
        } while (!vCorrecto);

        return valor;
    }

    public static void menuInstruccionesPagUno () {
        int opcion = 0;
        System.out.println("\t\t\t\t*** Instrucciones de la ruleta ***\n");
        System.out.println("1. [1-12] | [13-24] | [25-32]");
        System.out.println("\tSi el usuario apuesta en una de estas tres opciones y el número ganador aparece\n" +
                "\tentre los números que ha apostado, el usuario gana la cantidad de dinero apostada\n" +
                "\tpor el triple, ya que la probabilidad de ganar esta entre 1/3.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Volver Menú || Siguiente --> 2\n", 1, 2);
        switch (opcion) {
            case 1:
                menuPrincipal();
                break;

            case 2:
                menuInstruccionesPagDos();
                break;
        }
    }

    public static void menuInstruccionesPagDos () {
        int opcion = 0;
        System.out.println("2. [1/3] | [2/3] | [3/3]");
        System.out.println("\tEl usuario puede elegir que fila quiere apostar, [1/3] corresponde a la fila de arriba.\n" +
                "\tLa segunda fila corresponde a [2/3] y la última fila corresponde a [3/3]. Si la fila que ha apostado\n" +
                "\tel usuario es la ganadora, el premio será la cantidad de dinero apostado por el triple.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Atras || Siguiente --> 2\n", 1, 2);


        switch (opcion) {
            case 1:
                menuInstruccionesPagUno();
                break;

            case 2:
                menuInstruccionesPagTres();
                break;

        }

    }

    public static void menuInstruccionesPagTres () {
        int opcion = 0;
        System.out.println("3. [1-18] | [19-36]");
        System.out.println("\tEl usuario tendrá dos opciones para apostar, si el número ganador corresponde a la opción gandora,\n" +
                "\tel usuario ganará la cantidad de dinero apostada por el doble.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Atras || Siguiente --> 2\n", 1, 2);


        switch (opcion) {
            case 1:
                menuInstruccionesPagDos();
                break;

            case 2:
                menuInstruccionesPagCuatro();
                break;

        }

    }

    public static void menuInstruccionesPagCuatro () {
        int opcion = 0;
        System.out.println("4. [PAR] | [IMPAR]");
        System.out.println("\tEl usuario tendrá dos opciones para apostar, si el número ganador es igual a la opción\n" +
                "\telegida, el usuario gana la cantidad apostado por el doble.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Atras || Siguiente --> 2\n", 1, 2);


        switch (opcion) {
            case 1:
                menuInstruccionesPagTres();
                break;

            case 2:
                menuInstruccionesPagCinco();
                break;

        }

    }

    public static void menuInstruccionesPagCinco () {
        int opcion = 0;
        System.out.println("5. [ROJO] | [NEGRO]");
        System.out.println("\tEl usuario dispone de dos opciones para apostar, si el número ganador corresponde con el color\n" +
                "\tapostado, el usuario gana la cantidad apostada por el doble.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Atras || Siguiente --> 2\n", 1, 2);

        switch (opcion) {
            case 1:
                menuInstruccionesPagCuatro();
                break;

            case 2:
                menuInstruccionesPagSeis();
                break;
        }

    }

    public static void menuInstruccionesPagSeis () {
        int opcion = 0;
        System.out.println("6. Apostar uno o más números");
        System.out.println("\tEl usaurio podrá apostar a uno o varios números de la ruleta y si el número ganador coincide\n" +
                "\tcon el número apostado, el usuario gana el dinero apostado por treinta y seis veces, pero si\n" +
                "\tel usuario ha apostado por mas números y coincide con el número ganador, el usuario ganará el\n" +
                "\tdinero apostado por treinta y seis divido entre la cantidad de números apostados.\n");

        opcion = leerIntInstrucciones("\t\t\t\t1 <-- Atras || Salir --> 2\n", 1, 2);

        switch (opcion) {
            case 1:
                menuInstruccionesPagCinco();
                break;

            case 2:
                menuPrincipal();
                break;
        }

    }

    public static int leerIntInstrucciones(String mensaje, int min, int max) {
        Scanner leer = new Scanner(System.in);
        boolean vCorrecto = false;
        int valor = 0;

        do {
            System.out.print(mensaje);
            vCorrecto = leer.hasNextInt();

            if (!vCorrecto) {
                System.out.println("ERROR: Valor no númerico");
            } else {
                valor = leer.nextInt();
                if (valor < min | valor > max) {
                    System.out.println("ERROR: Valor fuera de rango");
                    vCorrecto = false;
                }
            }
            leer.nextLine();
        } while (!vCorrecto);
        return valor;
    }
}