-- Creamos la linea para trabjar con convocatorias.
Insert into MELANBIDE_CONVOCATORIAS (ID,PRO_COD,PRO_COD_PLATEA,NUM_BOPV,FEC_CONS_LANBIDE,DECRETOCODIGO,DECRETODESCRIPCION,DECRETODESCRIPCIONEU,DECRETOFECPUBLICACION,DECRETOFECENTRADAVIGOR,DECRETOFECFINAPLICACION)
 values (4,'ORI','LAN62_ORI',NULL,to_date('01/01/2000','DD/MM/RRRR'),'CONV_ANTE-2021','Convocatorias LANBIDE Antes de 2021 - Texto Generico.','Lanbideren deialdiak 2021 baino lehen - Testu Orokorra.',to_date('01/01/2000','DD/MM/RRRR'),to_date('01/01/2000','DD/MM/RRRR'),to_date('31/12/2020','dd/MM/yyyy'));
Insert into MELANBIDE_CONVOCATORIAS (ID,PRO_COD,PRO_COD_PLATEA,NUM_BOPV,FEC_CONS_LANBIDE,DECRETOCODIGO,DECRETODESCRIPCION,DECRETODESCRIPCIONEU,DECRETOFECPUBLICACION,DECRETOFECENTRADAVIGOR,DECRETOFECFINAPLICACION)
 values (5,'ORI','LAN62_ORI',NULL,to_date('01/01/2021','DD/MM/RRRR'),'CONV_2021-2023','LANBIDE - Convocatoria de ayudas 2021-2023. CAPÍTULO II: desarrollo de acciones y servicios de orientación profesional dirigidos, con carácter general, a las personas usuarias de Lanbide.','LANBIDE - 2021-2023 laguntzen deialdia. II. KAPITULUA: Orokorrean Lanbideko erabiltzaileei zuzendutako orientazio profesionaleko ekintza eta zerbitzuak gauzatzeko laguntzak.', to_date('01/01/2021','DD/MM/RRRR'),to_date('01/01/2021','DD/MM/RRRR'),to_date('31/12/2023','dd/MM/yyyy'));
COMMIT;



