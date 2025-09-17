-- =============================================================================
-- MERCADOLIBRE ITEM DETAIL API - SAMPLE DATA
-- =============================================================================

-- Insertar Categorías
INSERT INTO categories (id, name, path_from_root) VALUES
                                                      ('MLA1648', 'Electrónicos, Audio y Video', 'Electrónicos, Audio y Video'),
                                                      ('MLA1055', 'Celulares y Teléfonos', 'Electrónicos, Audio y Video > Celulares y Teléfonos'),
                                                      ('MLA1648_COMP', 'Computación', 'Electrónicos, Audio y Video > Computación'),
                                                      ('MLA1574', 'Hogar, Muebles y Jardín', 'Hogar, Muebles y Jardín'),
                                                      ('MLA1430', 'Ropa y Accesorios', 'Ropa y Accesorios'),
                                                      ('MLA1276', 'Deportes y Fitness', 'Deportes y Fitness'),
                                                      ('MLA5726', 'Electrodomésticos', 'Electrodomésticos');

-- Insertar Vendedores
INSERT INTO sellers (id, nickname, permalink, registration_date, country_id, reputation_level, power_seller_status, transactions_completed, transactions_canceled, rating_positive, rating_negative, rating_neutral) VALUES
                                                                                                                                                                                                                         (12345, 'TECHSTORE_OFICIAL', 'https://perfil.mercadolibre.com.co/TECHSTORE_OFICIAL', '2020-01-15 10:00:00', 'CO', '5_green', 'platinum', 5420, 12, 0.98, 0.01, 0.01),
                                                                                                                                                                                                                         (67890, 'SAMSUNG_OFICIAL', 'https://perfil.mercadolibre.com.co/SAMSUNG_OFICIAL', '2018-03-20 09:30:00', 'CO', '5_green', 'platinum', 8500, 15, 0.99, 0.005, 0.005),
                                                                                                                                                                                                                         (11111, 'LEVI_STORE_AR', 'https://perfil.mercadolibre.com.co/LEVI_STORE_AR', '2019-08-10 14:20:00', 'CO', '4_light_green', 'gold', 2300, 45, 0.95, 0.03, 0.02),
                                                                                                                                                                                                                         (22222, 'DEPORTES_EXTREMOS', 'https://perfil.mercadolibre.com.co/DEPORTES_EXTREMOS', '2021-05-12 11:45:00', 'CO', '3_yellow', null, 850, 20, 0.92, 0.05, 0.03),
                                                                                                                                                                                                                         (33333, 'HOGAR_PREMIUM', 'https://perfil.mercadolibre.com.co/HOGAR_PREMIUM', '2017-11-30 16:10:00', 'CO', '5_green', 'gold_pro', 3200, 8, 0.97, 0.02, 0.01);

-- Insertar Productos (Items)
INSERT INTO items (id, title, price_amount, price_currency, price_decimals, condition_type, available_quantity, sold_quantity, permalink, status, category_id, seller_id, description, listing_type, buying_mode, free_shipping, local_pick_up, created_date, updated_date) VALUES
-- TECNOLOGÍA
('MLA123456789', 'iPhone 15 Pro 128GB Titanio Natural', 1299999, 'COP', 2, 'new', 25, 150, 'https://articulo.mercadolibre.com.co/MLA-123456789', 'active', 'MLA1055', 12345, 'El iPhone 15 Pro cuenta con el revolucionario chip A17 Pro, cámara principal de 48 MP y diseño en titanio aeroespacial. Incluye Dynamic Island, Always-On display y hasta 29 horas de reproducción de video.', 'gold_special', 'buy_it_now', true, true, '2024-01-01 10:00:00', '2024-01-10 15:30:00'),

('MLA234567890', 'Samsung Galaxy S24 Ultra 256GB Gris Titanio', 1199999, 'COP', 2, 'new', 18, 89, 'https://articulo.mercadolibre.com.co/MLA-234567890', 'active', 'MLA1055', 67890, 'Galaxy S24 Ultra con S Pen integrado, cámara de 200MP con zoom de hasta 100x, pantalla Dynamic AMOLED 2X de 6.8" y procesador Snapdragon 8 Gen 3.', 'gold_pro', 'buy_it_now', true, false, '2024-01-02 09:15:00', '2024-01-12 11:20:00'),

('MLA345678901', 'MacBook Air 13" M2 256GB Gris Espacial', 1599999, 'COP', 2, 'new', 12, 67, 'https://articulo.mercadolibre.com.co/MLA-345678901', 'active', 'MLA1648_COMP', 12345, 'MacBook Air con chip M2 de Apple, pantalla Liquid Retina de 13.6", hasta 18 horas de batería y diseño ultradelgado. Perfecto para trabajo y creatividad.', 'gold_special', 'buy_it_now', true, true, '2023-12-28 14:45:00', '2024-01-08 16:10:00'),

('MLA456789012', 'Sony WH-1000XM5 Auriculares Bluetooth con Cancelación de Ruido', 89999, 'COP', 2, 'new', 45, 203, 'https://articulo.mercadolibre.com.co/MLA-456789012', 'active', 'MLA1648', 12345, 'Auriculares premium con la mejor cancelación de ruido del mercado, 30 horas de batería, sonido Hi-Res Audio y tecnología DSEE Extreme.', 'gold_special', 'buy_it_now', true, true, '2024-01-03 08:30:00', '2024-01-11 09:45:00'),

-- HOGAR
('MLA567890123', 'Smart TV Samsung 55" 4K UHD Crystal UN55CU7000', 149999, 'COP', 2, 'new', 8, 34, 'https://articulo.mercadolibre.com.co/MLA-567890123', 'active', 'MLA1574', 67890, 'Smart TV Crystal UHD 4K con Tizen OS, HDR10+, Control por Voz, Gaming Hub y conectividad completa. Incluye control remoto inteligente.', 'gold_pro', 'buy_it_now', false, true, '2024-01-04 11:20:00', '2024-01-13 13:15:00'),

('MLA678901234', 'Aire Acondicionado Split Inverter 3000W Frío/Calor BGH', 129999, 'COP', 2, 'new', 15, 28, 'https://articulo.mercadolibre.com.co/MLA-678901234', 'active', 'MLA1574', 33333, 'Split inverter eficiencia energética A, tecnología Dual Inverter, filtro antibacterial, control WiFi y instalación incluida en AMBA.', 'gold', 'buy_it_now', false, false, '2024-01-05 15:10:00', '2024-01-14 10:30:00'),

-- MODA
('MLA789012345', 'Zapatillas Nike Air Max 270 Hombre Negro/Blanco', 34999, 'COP', 2, 'new', 50, 127, 'https://articulo.mercadolibre.com.co/MLA-789012345', 'active', 'MLA1430', 22222, 'Zapatillas Nike Air Max 270 con la unidad Air más grande de Nike, diseño inspirado en el Air Max 93 y comodidad todo el día. Disponible en todos los talles.', 'gold', 'buy_it_now', true, true, '2024-01-06 12:40:00', '2024-01-15 08:20:00'),

('MLA890123456', 'Jean Levi''s 501 Original Fit Azul Clásico', 24999, 'COP', 2, 'new', 80, 245, 'https://articulo.mercadolibre.com.co/MLA-890123456', 'active', 'MLA1430', 11111, 'El jean original que comenzó todo. Corte straight, 100% algodón, botones de metal y el ajuste perfecto que mejora con el tiempo.', 'gold_special', 'buy_it_now', true, false, '2024-01-07 09:55:00', '2024-01-16 14:25:00'),

-- DEPORTES
('MLA901234567', 'Bicicleta Mountain Bike Rodado 29 21 Velocidades', 89999, 'COP', 2, 'new', 6, 12, 'https://articulo.mercadolibre.com.co/MLA-901234567', 'active', 'MLA1276', 22222, 'Mountain bike con cuadro de aluminio, suspensión delantera, cambios Shimano, frenos a disco y llantas rodado 29. Ideal para todo terreno.', 'gold', 'buy_it_now', false, true, '2024-01-08 16:30:00', '2024-01-17 11:40:00'),

-- ELECTRODOMÉSTICOS
('MLA012345678', 'Heladera Samsung RT53 364L No Frost Silver', 179999, 'COP', 2, 'new', 5, 18, 'https://articulo.mercadolibre.com.co/MLA-012345678', 'active', 'MLA5726', 67890, 'Heladera No Frost con tecnología Twin Cooling Plus, 364L de capacidad, eficiencia energética A, dispensador de agua y garantía oficial Samsung.', 'gold_pro', 'buy_it_now', false, false, '2024-01-09 13:20:00', '2024-01-18 15:50:00');

-- Insertar Atributos de Productos
INSERT INTO item_attributes (item_id, attribute_id, attribute_name, attribute_value, attribute_unit, value_type) VALUES
-- iPhone 15 Pro
('MLA123456789', 'BRAND', 'Marca', 'Apple', null, 'string'),
('MLA123456789', 'MODEL', 'Modelo', 'iPhone 15 Pro', null, 'string'),
('MLA123456789', 'STORAGE', 'Almacenamiento', '128', 'GB', 'number'),
('MLA123456789', 'COLOR', 'Color', 'Titanio Natural', null, 'string'),
('MLA123456789', 'SCREEN_SIZE', 'Tamaño de pantalla', '6.1', 'pulgadas', 'number'),

-- Samsung Galaxy S24 Ultra
('MLA234567890', 'BRAND', 'Marca', 'Samsung', null, 'string'),
('MLA234567890', 'MODEL', 'Modelo', 'Galaxy S24 Ultra', null, 'string'),
('MLA234567890', 'STORAGE', 'Almacenamiento', '256', 'GB', 'number'),
('MLA234567890', 'COLOR', 'Color', 'Gris Titanio', null, 'string'),
('MLA234567890', 'RAM', 'Memoria RAM', '12', 'GB', 'number'),

-- MacBook Air M2
('MLA345678901', 'BRAND', 'Marca', 'Apple', null, 'string'),
('MLA345678901', 'MODEL', 'Modelo', 'MacBook Air M2', null, 'string'),
('MLA345678901', 'STORAGE', 'Almacenamiento', '256', 'GB', 'number'),
('MLA345678901', 'PROCESSOR', 'Procesador', 'Apple M2', null, 'string'),
('MLA345678901', 'SCREEN_SIZE', 'Tamaño de pantalla', '13.6', 'pulgadas', 'number'),

-- Sony Auriculares
('MLA456789012', 'BRAND', 'Marca', 'Sony', null, 'string'),
('MLA456789012', 'MODEL', 'Modelo', 'WH-1000XM5', null, 'string'),
('MLA456789012', 'CONNECTION', 'Conexión', 'Bluetooth 5.2', null, 'string'),
('MLA456789012', 'BATTERY', 'Batería', '30', 'horas', 'number'),

-- Smart TV Samsung
('MLA567890123', 'BRAND', 'Marca', 'Samsung', null, 'string'),
('MLA567890123', 'SCREEN_SIZE', 'Tamaño de pantalla', '55', 'pulgadas', 'number'),
('MLA567890123', 'RESOLUTION', 'Resolución', '4K UHD', null, 'string'),
('MLA567890123', 'SMART_TV', 'Smart TV', 'Tizen OS', null, 'string'),

-- Aire Acondicionado
('MLA678901234', 'BRAND', 'Marca', 'BGH', null, 'string'),
('MLA678901234', 'POWER', 'Potencia', '3000', 'W', 'number'),
('MLA678901234', 'TYPE', 'Tipo', 'Split Inverter', null, 'string'),
('MLA678901234', 'FEATURES', 'Características', 'Frío/Calor', null, 'string'),

-- Zapatillas Nike
('MLA789012345', 'BRAND', 'Marca', 'Nike', null, 'string'),
('MLA789012345', 'MODEL', 'Modelo', 'Air Max 270', null, 'string'),
('MLA789012345', 'COLOR', 'Color', 'Negro/Blanco', null, 'string'),
('MLA789012345', 'GENDER', 'Género', 'Hombre', null, 'string'),

-- Jean Levi's
('MLA890123456', 'BRAND', 'Marca', 'Levi''s', null, 'string'),
('MLA890123456', 'MODEL', 'Modelo', '501 Original Fit', null, 'string'),
('MLA890123456', 'COLOR', 'Color', 'Azul Clásico', null, 'string'),
('MLA890123456', 'MATERIAL', 'Material', '100% Algodón', null, 'string'),

-- Bicicleta
('MLA901234567', 'BRAND', 'Marca', 'Mountain Pro', null, 'string'),
('MLA901234567', 'WHEEL_SIZE', 'Rodado', '29', 'pulgadas', 'number'),
('MLA901234567', 'SPEEDS', 'Velocidades', '21', null, 'number'),
('MLA901234567', 'FRAME', 'Cuadro', 'Aluminio', null, 'string'),

-- Heladera Samsung
('MLA012345678', 'BRAND', 'Marca', 'Samsung', null, 'string'),
('MLA012345678', 'MODEL', 'Modelo', 'RT53', null, 'string'),
('MLA012345678', 'CAPACITY', 'Capacidad', '364', 'L', 'number'),
('MLA012345678', 'TYPE', 'Tipo', 'No Frost', null, 'string');

-- Insertar Imágenes de Productos
INSERT INTO item_pictures (item_id, picture_id, url, secure_url, size, max_size, quality, picture_order) VALUES
-- iPhone 15 Pro
('MLA123456789', 'pic_iphone_001', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_001.jpg', '500x500', '1200x1200', 'high', 1),
('MLA123456789', 'pic_iphone_002', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_002.jpg', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_002.jpg', '500x500', '1200x1200', 'high', 2),
('MLA123456789', 'pic_iphone_003', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_003.jpg', 'https://http2.mlstatic.com/D_NQ_NP_iphone15pro_003.jpg', '500x500', '1200x1200', 'high', 3),

-- Samsung Galaxy S24 Ultra
('MLA234567890', 'pic_samsung_001', 'https://http2.mlstatic.com/D_NQ_NP_galaxys24_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_galaxys24_001.jpg', '500x500', '1200x1200', 'high', 1),
('MLA234567890', 'pic_samsung_002', 'https://http2.mlstatic.com/D_NQ_NP_galaxys24_002.jpg', 'https://http2.mlstatic.com/D_NQ_NP_galaxys24_002.jpg', '500x500', '1200x1200', 'high', 2),

-- MacBook Air M2
('MLA345678901', 'pic_macbook_001', 'https://http2.mlstatic.com/D_NQ_NP_macbookm2_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_macbookm2_001.jpg', '500x500', '1200x1200', 'high', 1),
('MLA345678901', 'pic_macbook_002', 'https://http2.mlstatic.com/D_NQ_NP_macbookm2_002.jpg', 'https://http2.mlstatic.com/D_NQ_NP_macbookm2_002.jpg', '500x500', '1200x1200', 'high', 2),

-- Sony Auriculares
('MLA456789012', 'pic_sony_001', 'https://http2.mlstatic.com/D_NQ_NP_sony_wh1000xm5_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_sony_wh1000xm5_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Smart TV Samsung
('MLA567890123', 'pic_tv_001', 'https://http2.mlstatic.com/D_NQ_NP_samsung_tv55_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_samsung_tv55_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Aire Acondicionado
('MLA678901234', 'pic_ac_001', 'https://http2.mlstatic.com/D_NQ_NP_bgh_split_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_bgh_split_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Zapatillas Nike
('MLA789012345', 'pic_nike_001', 'https://http2.mlstatic.com/D_NQ_NP_nike_airmax270_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_nike_airmax270_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Jean Levi's
('MLA890123456', 'pic_levis_001', 'https://http2.mlstatic.com/D_NQ_NP_levis_501_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_levis_501_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Bicicleta
('MLA901234567', 'pic_bike_001', 'https://http2.mlstatic.com/D_NQ_NP_mountainbike_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_mountainbike_001.jpg', '500x500', '1200x1200', 'high', 1),

-- Heladera
('MLA012345678', 'pic_fridge_001', 'https://http2.mlstatic.com/D_NQ_NP_samsung_rt53_001.jpg', 'https://http2.mlstatic.com/D_NQ_NP_samsung_rt53_001.jpg', '500x500', '1200x1200', 'high', 1);

-- Insertar Información de Envío
INSERT INTO shipping_methods (item_id, method_id, name, type, cost, currency, free_shipping, estimated_min_days, estimated_max_days, local_pick_up) VALUES
                                                                                                                                                        ('MLA123456789', 1, 'Envío gratis', 'standard', 0, 'COP', true, 2, 5, true),
                                                                                                                                                        ('MLA123456789', 2, 'Envío express', 'express', 2500, 'COP', false, 1, 2, true),
                                                                                                                                                        ('MLA234567890', 1, 'Envío gratis', 'standard', 0, 'COP', true, 3, 6, false),
                                                                                                                                                        ('MLA345678901', 1, 'Envío gratis', 'standard', 0, 'COP', true, 2, 4, true),
                                                                                                                                                        ('MLA456789012', 1, 'Envío gratis', 'standard', 0, 'COP', true, 1, 3, true),
                                                                                                                                                        ('MLA567890123', 1, 'Envío acordar', 'special', 0, 'COP', false, 5, 10, true),
                                                                                                                                                        ('MLA678901234', 1, 'Instalación incluida AMBA', 'installation', 0, 'COP', false, 7, 14, false),
                                                                                                                                                        ('MLA789012345', 1, 'Envío gratis', 'standard', 0, 'COP', true, 2, 4, true),
                                                                                                                                                        ('MLA890123456', 1, 'Envío gratis', 'standard', 0, 'COP', true, 3, 5, false),
                                                                                                                                                        ('MLA901234567', 1, 'Envío a acordar', 'special', 5000, 'COP', false, 7, 15, true),
                                                                                                                                                        ('MLA012345678', 1, 'Envío e instalación', 'installation', 0, 'COP', false, 10, 20, false);

-- Insertar Información de Garantía
INSERT INTO item_warranty (item_id, warranty_type, warranty_time, warranty_description) VALUES
                                                                                            ('MLA123456789', 'Garantía oficial', '12 meses', 'Garantía oficial Apple Argentina con cobertura total'),
                                                                                            ('MLA234567890', 'Garantía oficial', '24 meses', 'Garantía oficial Samsung con servicio técnico autorizado'),
                                                                                            ('MLA345678901', 'Garantía oficial', '12 meses', 'Garantía oficial Apple con soporte técnico incluido'),
                                                                                            ('MLA456789012', 'Garantía oficial', '24 meses', 'Garantía oficial Sony con reemplazo directo'),
                                                                                            ('MLA567890123', 'Garantía del vendedor', '12 meses', 'Garantía del vendedor con servicio técnico'),
                                                                                            ('MLA678901234', 'Garantía oficial', '24 meses', 'Garantía oficial BGH con instalación incluida'),
                                                                                            ('MLA789012345', 'Garantía del vendedor', '6 meses', 'Garantía del vendedor contra defectos de fábrica'),
                                                                                            ('MLA890123456', 'Garantía oficial', '12 meses', 'Garantía oficial Levi''s contra defectos de fabricación'),
                                                                                            ('MLA901234567', 'Garantía del vendedor', '12 meses', 'Garantía sobre cuadro y componentes principales'),
                                                                                            ('MLA012345678', 'Garantía oficial', '24 meses', 'Garantía oficial Samsung con servicio técnico a domicilio');

-- Insertar Métodos de Pago
INSERT INTO payment_methods (item_id, installments_quantity, installments_rate, installment_amount, accepts_credit_card, accepts_debit_card, accepts_mercadopago) VALUES
                                                                                                                                                                      ('MLA123456789', 12, 0.0, 108333.25, true, true, true),
                                                                                                                                                                      ('MLA234567890', 12, 15.5, 112500.00, true, true, true),
                                                                                                                                                                      ('MLA345678901', 18, 0.0, 88888.83, true, true, true),
                                                                                                                                                                      ('MLA456789012', 6, 0.0, 14999.83, true, true, true),
                                                                                                                                                                      ('MLA567890123', 12, 25.0, 15625.00, true, false, true),
                                                                                                                                                                      ('MLA678901234', 6, 12.0, 22833.17, true, true, true),
                                                                                                                                                                      ('MLA789012345', 3, 0.0, 11666.33, true, true, true),
                                                                                                                                                                      ('MLA890123456', 6, 10.0, 4583.17, true, true, true),
                                                                                                                                                                      ('MLA901234567', 12, 30.0, 9750.00, true, false, true),
                                                                                                                                                                      ('MLA012345678', 18, 20.0, 12333.28, true, true, true);