# Trabajo Practico Integrador: Sistemas de Gestion de la Configuracion

Este repositorio contiene la implementación práctica del Trabajo Práctico Integrador sobre Sistemas de Gestión de la Configuración, utilizando Git, GitHub y la metodología Gitflow.

## Equipo de Desarrollo

* @PabloDLQ - Pablo Dellacqua
* @juancganis - Juan Cruz Ganis
* @Scilingo-Valentin - Valentin Scilingo
* @LucasProdo - Lucas Prodolliet

## Tecnologias y Requisitos

* Lenguaje: Java
* Versión: Java 21 (Distribución Temurin)
* CI/CD: GitHub Actions

## Compilacion y Ejecucion

El proyecto está configurado para ser compilado y ejecutado fácilmente. Los comandos base son:

1. Compilar las clases:
   ```bash
   javac java/src/*.java
   ```

2. Ejecutar la aplicación:
   ```bash
   java -cp java/src Main
   ```

## Flujo de Trabajo (Gitflow)

Este proyecto sigue el modelo Gitflow:
- main: Código en producción.
- develop: Rama de integración.
- feature/* (y variantes de nomenclatura implementadas por el equipo): Nuevas funcionalidades.
- release/*: Preparación de pases a producción.
- hotfix/*: Corrección de errores en producción.

Todo cambio hacia main o develop requiere un Pull Request (PR), el cual dispara nuestra Integración Continua (ci.yml) y asigna revisores automáticamente basados en el archivo CODEOWNERS.

> **Nota sobre el ciclo de vida de las ramas:**  
> Siguiendo las buenas prácticas profesionales de Gitflow y control de versiones, las ramas temporales de soporte del tipo `release/*` y `hotfix/*` fueron eliminadas del repositorio remoto una vez que sus cambios se fusionaron exitosamente a `main` y `develop`, respaldando el historial mediante *Tags* y el registro de *merges*. Excepcionalmente, se optó por mantener visibles en el repositorio remoto las ramas de desarrollo de funcionalidades (con sus respectivas nomenclaturas iniciales tales como `feature1`, `Feature_2`, etc.) para facilitar la trazabilidad y la corrección académica del trabajo práctico.

## Respuestas a la Consigna

### 7.b ¿Cómo hacemos para no subir cambios de configuraciones locales?
Para evitar subir archivos de configuración local, se debe crear un archivo llamado .gitignore en la raíz del repositorio. En este archivo de texto se listan los nombres, rutas o patrones de los archivos y carpetas que Git debe omitir (por ejemplo, configuraciones del entorno de desarrollo, variables locales o archivos compilados). Al incluirlos en el .gitignore, Git no hará un seguimiento de sus cambios y nunca los incluirá al momento de realizar un commit o un push al repositorio remoto.

### 7.e Llevar al entorno productivo Release 1. ¿Cómo lo hace siguiendo Gitflow?
Siguiendo Gitflow, para llevar un "Release 1" al entorno productivo se deben realizar los siguientes pasos de integración:
   1. Merge a main (o master): Se debe fusionar (merge) la rama de release hacia la rama main. En Gitflow, la rama main refleja siempre el estado de producción.
   2. Crear un Tag (Etiqueta): Sobre el commit resultante en main, se debe crear una etiqueta con el número de versión (por ejemplo, git tag v1.0.0) para dejar un registro histórico exacto del lanzamiento.
   3. Merge a develop: Se debe fusionar la rama de release de vuelta hacia la rama develop. Esto es fundamental para asegurar que cualquier corrección de errores de último momento realizada en la rama de release quede incorporada en el código base para futuros desarrollos.
   4. Eliminar la rama: Una vez realizados ambos merges, la rama de release se elimina, ya que cumplió su ciclo de vida.

### 7.f Se encontró un error en la versión productiva, ¿Cómo lo corregimos?
Siguiendo Gitflow, los errores críticos en el entorno productivo se corrigen utilizando una rama de tipo hotfix. El procedimiento exacto es el siguiente:
   1. Crear la rama: Se crea una nueva rama (por ejemplo, hotfix/fix-error-login) tomando como base exclusiva la rama main, ya que es la que refleja el estado actual de producción.
   2. Aplicar la corrección: Se realizan los cambios necesarios para solucionar el problema y se registran (commit) en esta nueva rama.
   3. Merge a main y Tag: Se fusiona (merge) la rama hotfix de regreso a main para desplegar la corrección en producción. Sobre este nuevo commit se debe crear un Tag incrementando la versión del parche (por ejemplo, de v1.0.0 a v1.0.1).
   4. Merge a develop: Se fusiona la misma rama hotfix hacia develop para asegurar que el error quede corregido en el código base de los futuros desarrollos y no vuelva a aparecer en el próximo release.
   5. Eliminar la rama: Una vez realizados ambos merges, la rama hotfix se elimina.

### 7.j Llevar los cambios de la rama creada en el punto 1 a producción. ¿Cómo lo hace siguiendo Gitflow?
Siguiendo Gitflow, una rama de nueva funcionalidad (feature branch) nunca se integra directamente a producción. Para llevar esos cambios al entorno productivo, se debe seguir este flujo:
   1. Merge a develop: Finalizado el desarrollo, la rama con la nueva funcionalidad se fusiona hacia la rama develop y, generalmente, se elimina.
   2. Crear una rama de Release: Una vez que develop contiene esta y otras funcionalidades listas para salir, se crea una nueva rama release a partir de ella.
   3. Merge a main y Tag: La rama release se fusiona hacia main, llevando efectivamente los cambios a producción. Sobre ese commit en main se genera un Tag con el número de la nueva versión (por ejemplo, v1.1.0).
   4. Merge a develop: Finalmente, la rama release se fusiona de regreso hacia develop para asegurar que todo quede sincronizado y se elimina la rama.

### 8.a. ¿Que documentarian en un README?

En el README de un proyecto documentaríamos:
- Propósito general del proyecto.
- Requisitos técnicos y dependencias (ej. Java 21).
- Instrucciones claras para la instalación y ejecución local.
- Explicación del flujo de trabajo y ramas (Gitflow).
- Políticas para contribuir (commits, PRs) y responsables del código.

### 8.b. Si un externo al equipo realiza una modificación, necesitamos entender el cambio realizado

**¿Qué datos le pediría a esa persona que complete?**
Le pediríamos:
- Título descriptivo del cambio.
- Motivación del cambio y problema que resuelve.
- Pasos para probar y validar los cambios localmente.

**¿Qué nos ofrece GitHub para ayudarnos con esto?**
Para resolver esto, GitHub ofrece los Pull Request Templates, una herramienta que permite predefinir una estructura de texto para las descripciones de los PR. El racional detrás de esto es garantizar que cualquier desarrollador externo justifique sus cambios y reporte el estado de sus pruebas desde el primer momento, optimizando el tiempo de revisión del equipo principal.
