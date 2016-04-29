package utility


import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import static org.apache.commons.csv.CSVFormat.DEFAULT

import java.nio.file.Paths

class InputProvider{
	
   def lines
   public List readCsv(path){
	  def data = []
	  def defaultPathBase = new File( "." ).getCanonicalPath() + "/src/sysTest/groovy/resources/" 

      CSVParser parser = new CSVParser(new FileReader(defaultPathBase + path), DEFAULT.withHeader())
	  //println parser.toList().toString()
	  for (CSVRecord record : parser) {
			data << record.values
		}
	  //return parser.toList()
	  return data
		 
                  
 }
}