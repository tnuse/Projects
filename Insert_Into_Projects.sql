INSERT INTO category (category_name) VALUES ("V-8 Engine Rebuild");

INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Head Gasket", 2);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Header Gasket", 2);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Spark Plugs", 8);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Quart of Oil", 5);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Spark Plug Wires", 8);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Intake Manifold Gasket", 1);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Carburetor Gasket", 1);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, "Valve Cover Gasket", 8);


INSERT INTO step (project_id, step_order, step_text) VALUES (1, 1, "Remove all hoses and wires from the Engine");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 2, "Remove Valve Cover Gaskets");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 3, "Unbolt Carburetor from Intake manifold");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 4, "Unbolt Exhaust Mainfolds from Heads");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 5, "Remove all spark plug wires and spark plugs from Heads");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 6, "Remove Intake Manifold");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 7, "Remove Heads");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 8, "Remove HEI Distributor");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 9, "Scrape old gasket material from Heads, Exhaust Manifolds, Carburetor, Valve Covers, and Intake Manifold");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 10, "Using new gaskets, bolt Head, Exhaust Manifolds, Carburetor, Valve Covers, and Intake Manifold to the new Engine block");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 11, "Install HEI Distributor");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 12, "Install new Spark Plugs into Heads");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 13, "Install new Spark Plug Wires and connect to Spark Plugs to accommodate the correct firing order");
INSERT INTO step (project_id, step_order, step_text) VALUES (1, 14, "Add oil to the Engine");

