del /f /q /s bin
rmdir bin /q /s
mkdir bin
javac -cp postgresql-42.7.3.jar src\Business\*.java src\Data\*.java src\Presentation\*.java -d bin
