package com.ctrlhub.core

import com.ctrlhub.core.assets.equipment.exposures.resource.EquipmentExposureResource
import com.ctrlhub.core.assets.equipment.resource.EquipmentCategory
import com.ctrlhub.core.assets.equipment.resource.EquipmentItem
import com.ctrlhub.core.assets.equipment.resource.EquipmentManufacturer
import com.ctrlhub.core.assets.equipment.resource.EquipmentModel
import com.ctrlhub.core.assets.vehicles.resource.Vehicle
import com.ctrlhub.core.assets.vehicles.resource.VehicleCategory
import com.ctrlhub.core.assets.vehicles.resource.VehicleManufacturer
import com.ctrlhub.core.assets.vehicles.resource.VehicleModel
import com.ctrlhub.core.assets.vehicles.resource.VehicleSpecification
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.FormSubmission
import com.ctrlhub.core.geo.Property
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.projects.appointments.response.Appointment
import com.ctrlhub.core.projects.operations.response.Operation

/**
 * Centralised place to register JSON:API resource `type` -> Kotlin class mappings.
 * Call `ResourceTypeRegistry.registerDefaults()` during SDK initialization or in tests.
 */
object ResourceTypeRegistry {
    fun registerDefaults() {
        // media
        FormSubmissionVersion.registerResourceType("images", Image::class.java)

        // operations / projects
        FormSubmissionVersion.registerResourceType("operations", Operation::class.java)
        FormSubmissionVersion.registerResourceType("appointments", Appointment::class.java)

        // users / iam
        FormSubmissionVersion.registerResourceType("users", User::class.java)

        // datacapture
        FormSubmissionVersion.registerResourceType("forms", Form::class.java)
        FormSubmissionVersion.registerResourceType("form-schemas", FormSchema::class.java)
        FormSubmissionVersion.registerResourceType("form-submissions", FormSubmission::class.java)
        FormSubmissionVersion.registerResourceType("form-submission-versions", FormSubmissionVersion::class.java)

        // properties / geo
        FormSubmissionVersion.registerResourceType("properties", Property::class.java)

        // vehicles
        FormSubmissionVersion.registerResourceType("vehicles", Vehicle::class.java)
        FormSubmissionVersion.registerResourceType("vehicle-categories", VehicleCategory::class.java)
        FormSubmissionVersion.registerResourceType("vehicle-specifications", VehicleSpecification::class.java)
        FormSubmissionVersion.registerResourceType("vehicle-manufacturers", VehicleManufacturer::class.java)
        FormSubmissionVersion.registerResourceType("vehicle-models", VehicleModel::class.java)

        // equipment
        FormSubmissionVersion.registerResourceType("equipment-items", EquipmentItem::class.java)
        FormSubmissionVersion.registerResourceType("equipment-models", EquipmentModel::class.java)
        FormSubmissionVersion.registerResourceType("equipment-categories", EquipmentCategory::class.java)
        FormSubmissionVersion.registerResourceType("equipment-manufacturers", EquipmentManufacturer::class.java)
        FormSubmissionVersion.registerResourceType("equipment-exposures", EquipmentExposureResource::class.java)
    }
}

