package groovy.analysis

import app.analysis.ClassificationModule
import app.structures.CommitDetails
import app.structures.FileDiffs
import org.joda.time.DateTime
import spock.lang.Specification

class ClassificationModuleSpec extends Specification{
    def "should create correct classification"(){
        setup:
        def module = new ClassificationModule()
        def commitDetails = new ArrayList<CommitDetails>()

        def cm1 = new CommitDetails()
        cm1.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1), "xyz", "Commit message 1.1", "master")
        def fd11 = new FileDiffs()
        fd11.setInformation("file1.java", 20, 2)
        cm1.addFile(fd11)
        def fd12 = new FileDiffs()
        fd12.setInformation("file2.java", 30, 3)
        cm1.addFile(fd12)

        def cm2 = new CommitDetails()
        cm2.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "xyz", "Commit message 2.1", "master")
        def fd21 = new FileDiffs()
        fd21.setInformation("file3.java", 10, 8)
        cm2.addFile(fd21)
        def fd22 = new FileDiffs()
        fd22.setInformation("file4.java", 25, 8)
        cm2.addFile(fd22)
        def fd23 = new FileDiffs()
        fd23.setInformation("file5.java", 12, 1)
        cm2.addFile(fd23)

        def cm3 = new CommitDetails()
        cm3.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "abc", "Commit message 1.1", "branch")
        def fd31 = new FileDiffs()
        fd31.setInformation("file6.java", 10, 3)
        cm3.addFile(fd31)
        def fd32 = new FileDiffs()
        fd32.setInformation("file7.java", 19, 17)
        cm3.addFile(fd32)
        def fd33 = new FileDiffs()
        fd33.setInformation("file8.java", 14, 13)
        cm3.addFile(fd33)

        commitDetails.addAll([cm1, cm2, cm3])

        def expectedCommitsResult = new HashMap<String, Integer>()
        expectedCommitsResult.put("xyz", 2)
        expectedCommitsResult.put("abc", 1)

        def expectedInsertionsResult = new HashMap<String, Integer>()
        expectedInsertionsResult.put("xyz", 97)
        expectedInsertionsResult.put("abc", 43)

        def expectedDeletionsResult = new HashMap<String, Integer>()
        expectedDeletionsResult.put("abc", 33)
        expectedDeletionsResult.put("xyz", 22)

        when:
        def commitsResult = module.countCommits(commitDetails, new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1), new DateTime().withYear(2017).withMonthOfYear(12).withDayOfMonth(31))
        def insertionsResult = module.countInsertions(commitDetails, new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1), new DateTime().withYear(2017).withMonthOfYear(12).withDayOfMonth(31))
        def deletionsResult = module.countDeletions(commitDetails, new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1), new DateTime().withYear(2017).withMonthOfYear(12).withDayOfMonth(31))

        then:
        for(String key: expectedCommitsResult.keySet())
            assert expectedCommitsResult.get(key) == commitsResult.get(key)

        for(String key: expectedInsertionsResult.keySet())
            assert expectedInsertionsResult.get(key) == insertionsResult.get(key)

        for(String key: expectedDeletionsResult.keySet())
            assert expectedDeletionsResult.get(key) == deletionsResult.get(key)
    }
}