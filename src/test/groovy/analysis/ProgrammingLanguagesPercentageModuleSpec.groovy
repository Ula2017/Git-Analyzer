package groovy.analysis

import app.analysis.ProgrammingLanguagesPercentageModule
import app.structures.CommitDetails
import app.structures.FileDiffs
import org.joda.time.DateTime
import spock.lang.Specification

class ProgrammingLanguagesPercentageModuleSpec extends Specification{
    def "should create correct dataset"(){
        setup:
        def module = new ProgrammingLanguagesPercentageModule()

        def commitDetails = new ArrayList<CommitDetails>()
        def cm1 = new CommitDetails()
        cm1.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(1), "karolb", "Commit message 1.1", "master")
        def fd11 = new FileDiffs()
        fd11.setInformation("lala1.js", 20, 2)
        cm1.addFile(fd11)
        def fd12 = new FileDiffs()
        fd12.setInformation("lala2.cs", 30, 3)
        cm1.addFile(fd12)
        def fd13 = new FileDiffs()
        fd13.setInformation("lala3.html", 40, 4)
        cm1.addFile(fd13)
        def fd14 = new FileDiffs()
        fd14.setInformation("lala4.txt", 1, 1)
        cm1.addFile(fd14)
        def fd15 = new FileDiffs()
        fd15.setInformation("lala5.js", 20, 30)
        cm1.addFile(fd15)

        def cm2 = new CommitDetails()
        cm2.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "chudy1997", "Commit message 1.1", "master")
        def fd21 = new FileDiffs()
        fd21.setInformation("lele1.js", 10, 3)
        cm2.addFile(fd21)
        def fd22 = new FileDiffs()
        fd22.setInformation("lele2.js", 10, 17)
        cm2.addFile(fd22)
        def fd23 = new FileDiffs()
        fd23.setInformation("lele3.cs", 10, 13)
        cm2.addFile(fd23)

        commitDetails.addAll([cm1, cm2])

        def expectedResult = new HashMap<String, Integer>()
        expectedResult.put(".js", 8)
        expectedResult.put(".cs", 24)
        expectedResult.put(".html", 36)


        when:
        def result = module.getLinesForLanguages(commitDetails)

        then:
        for(String key: expectedResult.keySet())
            assert expectedResult.get(key) == result.get(key)
    }
}