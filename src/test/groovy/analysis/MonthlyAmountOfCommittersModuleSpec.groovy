package groovy.analysis

import app.analysis.MonthlyAmmountOfCommitersModule
import app.structures.CommitDetails
import org.joda.time.DateTime
import spock.lang.Specification

class MonthlyAmountOfCommittersModuleSpec extends Specification{
    def "should create correct dataset"(){
        setup:
        def module = new MonthlyAmmountOfCommitersModule()

        def commitDetails = new ArrayList<CommitDetails>()
        def cm11 = new CommitDetails()
        cm11.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(11), "karolb", "Commit message 1.1", "master")
        commitDetails.add(cm11)
        def cm12 = new CommitDetails()
        cm12.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(12), "chudy1997", "Commit message 1.2", "master")
        commitDetails.add(cm12)
        def cm13 = new CommitDetails()
        cm13.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(13), "lolek308", "Commit message 1.3", "master")
        commitDetails.add(cm13)
        def cm21 = new CommitDetails()
        cm21.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(25), "karolb", "Commit message 2.1", "master")
        commitDetails.add(cm21)
        def cm22 = new CommitDetails()
        cm22.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(26), "chudy1997", "Commit message 2.1", "master")
        commitDetails.add(cm22)
        def cm31 = new CommitDetails()
        cm31.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(1).withDayOfMonth(1), "karolb", "Commit message 3.1", "master")
        commitDetails.add(cm31)
        def cm32 = new CommitDetails()
        cm32.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(1).withDayOfMonth(31), "karolb", "Commit message 3.2", "master")
        commitDetails.add(cm32)
        def cm41 = new CommitDetails()
        cm41.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(5).withDayOfMonth(31), "karolb", "Commit message 4.1", "master")
        commitDetails.add(cm41)

        def symbolAxis = new ArrayList<String>()
        def expectedResult = [3, 2, 1, 0, 0, 0, 1]

        when:
        def result = module.countAuthors(commitDetails, new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(11),
                new DateTime().withYear(2017).withMonthOfYear(5).withDayOfMonth(31), symbolAxis)

        then:
        for(int i=0;i<result.size();i++)
            assert result.get(i) == expectedResult[i]
    }
}