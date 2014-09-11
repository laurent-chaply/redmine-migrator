package net.kaleidos.taiga

import net.kaleidos.domain.Membership
import net.kaleidos.domain.Project

class ProjectTaigaSpec extends TaigaSpecBase {

    void 'get a project'() {
        given: 'an existing project'
            def project = new Project().setName("name ${new Date().time}").setDescription("description")
            project = taigaClient.createProject(project)

            assert taigaClient.getProjects().size() > 0
        when: 'tying to get the project'
            def sameProject = taigaClient.getProjectById(project.id)

        then: 'the projects are the same'
            project.id == sameProject.id

        cleanup:
            taigaClient.deleteProject(project)
    }

    void 'save a simple project'() {
        given: 'a project to save'
            def project = new Project()
                .setName(name)
                .setDescription(description)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved'
            project != null
            project.id != null
            project.name == name
            project.description == description
            project.issueStatuses.size() == 0
            project.issueTypes.size() == 0
            project.issuePriorities.size() == 0
            project.issueSeverities.size() == 0
            project.roles.size() == 0

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
    }

    void 'save a project with and old start date'() {
        given: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setCreatedDate(Date.parse("dd/MM/yyyy", createdDate))

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved with the old date'
            project.createdDate != null
            project.createdDate.format("dd/MM/yyyy") == createdDate

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            createdDate = "01/01/2010"
    }

    void 'save a project with issue types, statuses, priorities and severities'() {
        given: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setIssueTypes(types)
                .setIssueStatuses(statuses)
                .setIssuePriorities(priorities)
                .setIssueSeverities(severities)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved with all the fields'
            project.issueTypes.size() == types.size()
            project.issueTypes.sort() == types.sort()
            project.issueStatuses.size() == statuses.size()
            project.issueStatuses*.name.sort() == statuses*.name.sort()
            project.issueStatuses*.isClosed.sort() == statuses*.isClosed.sort()
            project.issuePriorities.size() == priorities.size()
            project.issuePriorities.sort() == priorities.sort()
            project.issueSeverities.size() == severities.size()
            project.issueSeverities.sort() == severities.sort()

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            types = ['Bug', 'Question', 'Enhancement']
            statuses = buildIssueStatuses()
            priorities = ['Low', 'Normal', 'High']
            severities = ['Minor', 'Normal', 'Important', 'Critical']
    }

    void 'save a project with roles'() {
        given: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setRoles(roles)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved with all the fields'
            project.roles.size() == roles.size()
            project.roles.sort() == roles.sort()

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            roles = ['UX', 'Design', 'Front', 'Back']
    }

    void 'save a project with memberships'() {
        given: 'some memberships to add to a project'
            def m1 = new Membership().setEmail('user1@example.com').setRole('UX')
            def m2 = new Membership().setEmail('user2@example.com').setRole('Back')
            def memberships = [m1, m2]

        and: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setMemberships(memberships)
                .setRoles(roles)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved with the members and the creator of the proyect (owner)'
            project.memberships.size() == memberships.size() + 1
            project.memberships*.email.contains(memberships[0].email)
            project.memberships*.email.contains(memberships[1].email)

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            roles = ['UX', 'Back']
    }

    void 'save a project with user stories statuses'() {
        given: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setUserStoryStatuses(usStatuses)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved'
            project != null
            project.userStoryStatuses.size() == usStatuses.size()
            project.userStoryStatuses*.name.sort() == usStatuses*.name.sort()
            project.userStoryStatuses*.isClosed.sort() == usStatuses*.isClosed.sort()

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            usStatuses = buildUserStoryStatuses()
    }

    void 'save a project with estimation points'() {
        given: 'a project to create'
            def project = new Project()
                .setName(name)
                .setDescription(description)
                .setPoints(points)

        when: 'saving the project'
            project = taigaClient.createProject(project)

        then: 'the project is saved'
            project != null
            project.points.size() == points.size()
            project.points*.name.sort() == points*.name.sort()
            project.points*.value.sort() == points*.value.sort()

        cleanup:
            taigaClient.deleteProject(project)

        where:
            name = "My project ${new Date().time}"
            description = 'The description of the project'
            points = buildEstimationPoints()
    }
}