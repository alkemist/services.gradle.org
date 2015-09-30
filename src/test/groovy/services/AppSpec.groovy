package services

import ratpack.test.MainClassApplicationUnderTest
import spock.lang.AutoCleanup
import spock.lang.Specification

class AppSpec extends Specification {

    @AutoCleanup
    def app = new MainClassApplicationUnderTest(Main)

    def httpClient = app.httpClient

    def setup() {
        httpClient.requestSpec {
            it.redirects(0)
        }
    }

    def "redirects for distros"() {
        when:
        def resp = httpClient.get("distributions/gradle-2.4-bin.zip")

        then:
        resp.statusCode == 301
        resp.headers["Location"] == "https://downloads.gradle.org/distributions/gradle-2.4-bin.zip"
    }

    def "redirects for distro snaps"() {
        when:
        def resp = httpClient.get("distributions-snapshots/gradle-2.4-bin.zip")

        then:
        resp.statusCode == 301
        resp.headers["Location"] == "https://downloads.gradle.org/distributions-snapshots/gradle-2.4-bin.zip"
    }

    def "serves app json"() {
        when:
        def resp = httpClient.get("versions/all")

        then:
        resp.body.contentType.json
        resp.body.text == getClass().classLoader.getResource("versions.json").text
    }
}
