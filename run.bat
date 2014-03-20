
::java addLines/AddLines < numberdata.txt > addLinesClasstest.out
::java -jar addLines/addLines numberdata.txt > addLinesFolderJartest.out
::java -jar addLines numberdata.txt > addLinesJARtest.out
::java.exe -jar addLines numberdata.txt > addLinesJAR_EXEtest.out
::java avgFile/AverageFile < addLinesClasstest.out > avgLinesClasstest.out
::java addLines/AddLines < numberdata.txt | avgFile/AvgFile > avgLinesClasstest.out

java -jar myBatchProcessor.jar work/batch1.xml
java -jar myBatchProcessor.jar work/batch2.xml
java -jar myBatchProcessor.jar work/batch3.xml
::java -jar myBatchProcessor.jar work/batch4.xml
java -jar myBatchProcessor.jar work/batch5.broken.xml
::examples/TestFunctionality ../batch1.xml
::java examples/TestFunctionality ../batch2.xml
::java examples/TestFunctionality ../batch3.xml
::java examples/TestFunctionality ../batch4.xml
::java examples/TestFunctionality ../batch5.broken.xml

::java examples/XmlParsing ../work/batch1.xml
::java examples/XmlParsing ../work/batch2.xml
::java examples/XmlParsing ../work/batch3.xml
::java examples/XmlParsing ../work/batch4.xml
::java examples/XmlParsing ../work/batch5.xml

pause > nul
