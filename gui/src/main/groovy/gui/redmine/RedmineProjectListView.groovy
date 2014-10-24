package gui.redmine

import gui.controller.*
import com.taskadapter.redmineapi.bean.Project

import org.viewaframework.annotation.*
import org.viewaframework.widget.view.*
import org.viewaframework.widget.view.ui.*
import org.viewaframework.view.DefaultViewContainer

@Controllers([
    @Controller(type=RedmineMigrationWarningController,pattern='migrateSelected'),
    @Controller(type=RedmineProjectListController,pattern='refresh')
])
class RedmineProjectListView extends MasterView<Project> {

    static final ID = 'projectListViewID'

    RedmineProjectListView() {
        super(ID, [
            new MasterViewColumn("id", 20),
            new MasterViewColumn("name", 200),
            new MasterViewColumn("identifier", 100),
            new MasterViewColumn("description", 400),
            new MasterViewColumn("createdOn", 50),
            new MasterViewColumn("updatedOn", 50),
        ])
    }

    Class<Project> getMasterType() {
        return Project
    }

}