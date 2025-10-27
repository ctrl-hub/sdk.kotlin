package com.ctrlhub.core.datacapture

import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.json.JsonConfig
import com.fasterxml.jackson.core.type.TypeReference
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FormSchemaRawSchemaTest {

    @Test
    fun `rawSchema should produce jsonapi envelope with expected fields`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/form-schema-sample.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mapper = JsonConfig.getMapper()
        val root = mapper.readTree(jsonContent)
        val dataNode = root.get("data")
        val id = dataNode.get("id").asText()

        val attributesNode = dataNode.get("attributes")
        // Convert attributes node to a Map<String, Any>
        val attributesMap = mapper.convertValue(attributesNode, object : TypeReference<Map<String, Any>>() {})

        val model = attributesMap["model"] as? Map<String, Any>
        val view = attributesMap["view"] as? Map<String, Any>
        val version = attributesMap["version"] as? String

        // Construct the FormSchema using the parsed pieces (meta omitted here)
        val fs = FormSchema(id = id, model = model, view = view, version = version, meta = null)
        val raw = fs.rawSchema

        // Parse the generated JSON and assert structure
        val node = mapper.readTree(raw)

        assertEquals(id, node["id"].asText())
        assertEquals("form-schemas", node["type"].asText())

        val attributes = node["attributes"]
        assertEquals(version, attributes["version"].asText())
        assertTrue(attributes["model"]["properties"].has("field-1"))
    }
}
