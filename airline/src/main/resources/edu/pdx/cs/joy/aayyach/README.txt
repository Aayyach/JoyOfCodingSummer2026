Developer: 
Alaa Ayyach

Assignment:
Project 1/5 | Designing an Airline Application | CS410P Joy of Coding Java and Android at PSU

Description:
This is Project 1 of 5 assigned in the Joy of Coding Java and Android at Portland State University. 
It consists of setting up the Flight and Airline classes with the correct fields and methods, 
providing javadoc comments for all classes and methods written, and unit testing to ensure correct
program behavior. 

There is also a Project1 class that consists of the main method along with two three methods I wrote:
isValidDateAndTime, printHelpfulDesc, and printREADMEFile. The main method parses the command line, 
creates Airline and Flight objects, and adds the Flight to the Airline in the case that everything
was entered correctly on the command line. The Project1 class differs from Airline and Flight in 
that it requires both unit and integration testing since it has the main method and controls the
flow of the program. 

Give the program a try! Here's how you run it:

usage: java -jar target/airline-1.0.0.jar [options] <args>
args are (in this order):
airline         The name of the airline
flightNumber    The flight number
src             Three-letter code of departure airport
depart          Departure date and time (24-hour time)
dest            Three-letter code of arrival airport
arrive          Arrival date and time (24-hour time)
options are (options may appear in any order):
-print          Prints a description of the new flight
-README         Prints a README for this project and exits
Date and time should be in the format: mm/dd/yyyy hh:mm