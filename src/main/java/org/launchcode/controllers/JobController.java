package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("job", job);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors, RedirectAttributes attributes) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            return "new-job";
        }

        // get job form properties to create a new job
        String name = jobForm.getName();
        int empId = jobForm.getEmployerId();
        int locId = jobForm.getLocationId();
        int posId = jobForm.getPositionTypeId();
        int coreId = jobForm.getCoreCompetenciesId();

        //  create job properties from job form
        String newName = name;
        Employer newEmp = jobData.getEmployers().findById(empId);
        Location newLoc = jobData.getLocations().findById(locId);
        PositionType newPos = jobData.getPositionTypes().findById(posId);
        CoreCompetency newComp = jobData.getCoreCompetencies().findById(coreId);

        // construct new job
        Job newJob = new Job(newName, newEmp, newLoc, newPos, newComp);

        // add the new job to the jobData
        jobData.add(newJob);

        attributes.addAttribute("id", newJob.getId());
        return "redirect:/job";

    }
}