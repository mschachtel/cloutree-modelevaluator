library(pmml)

linModel <- lm(VALUE ~ NAME, test.learning.data)
pmml(linModel)
saveXML(pmml(linModel), "testPMML.xml")

pred1 <- predict(linModel, test.input.data)

pred1