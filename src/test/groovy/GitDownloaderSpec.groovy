package groovy

import app.fetch.GitDownloader
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.api.errors.JGitInternalException
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class GitDownloaderSpec extends Specification{

     def "should return git object"(){
         setup:
            def gitDownloader = new GitDownloader("https://github.com/centic9/jgit-cookbook")
         when:
            def result = gitDownloader.getRepository()
         then:
            result.class == org.eclipse.jgit.api.Git
     }

    def "should return true if correctly downloaded repo"(){
        setup:
            def gitDownloader = new GitDownloader("https://github.com/centic9/jgit-cookbook")
        when:
            gitDownloader.getRepository()
        then:
            Files.exists(Paths.get("C:\\localRepo"))
    }

    def "should not return JGitInternalException if gitDownloader correctly deletes path"(){
        setup:
            new File("C:\\localRepo").mkdir()
            def gitDownloader = new GitDownloader("https://github.com/centic9/jgit-cookbook")
        when:
            gitDownloader.getRepository()
        then:
            notThrown(JGitInternalException)
    }

    def "should not return GitApiException if gitDownloader correctly deletes path"(){
        setup:
            new File("C:\\localRepo").mkdir()
            def gitDownloader = new GitDownloader("https://github.com/centic9/jgit-cookbook")
        when:
            gitDownloader.getRepository()
        then:
            notThrown(GitAPIException)
    }

    def "should return true if repo exists"(){
        setup:
            def gitDownloader = new GitDownloader("https://github.com/centic9/jgit-cookbook")
        when:
            def result = gitDownloader.checkIfExistsRemote()
        then:
            result
    }

    def "should return false if repo doesnt exist"(){
        setup:
            def gitDownloader = new GitDownloader("ThisIsNotValidLinkToRepo")
        when:
            def result = gitDownloader.checkIfExistsRemote()
        then:
            !result

    }

}