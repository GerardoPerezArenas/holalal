TB_Padding = 3;
TB_Borde = 1;
TB_Fondo = '#FFFFFF';
TB_Fuente = '#000000';
TB_FondoActivo = '#E6E6E6';
TB_FuenteActivo ='#000000';
TB_FondoObs='#FFFFCC';
TB_ColorCabecera = '#DCDCCC';
function isUndefined(a) { return ((typeof a == 'undefined') || (a==null)); }
var TB_ImgPath = APP_CONTEXT_PATH + "/images/";
var TB_Num = 0;
if(document.all) cursor = 'hand';
else cursor = 'pointer';
document.writeln('<style>');
document.writeln('</style>');

function tb_addElement(lista,elemento) {
    var i = lista.length;
    lista[i] = elemento;
}

function tb_deleteElement(lista,index){
  if(index<0 || index >= lista.length) return null;
  var val = lista[index];
  var i, j;
  for (i=eval(index);i < (lista.length-1);i++){
    j = i + 1;
    lista[i] = lista[j];
  }
  lista.length--;
  return val;
}

function Lista(){
	this.items = new Array();
	this.pop = function(item){
		var newItems = new Array();
		for(var i=0; i < this.items.length; i++){
			if(this.items[i]!=item) newItems[newItems.length] = this.items[i];
		}
		this.items = newItems;
	}
	this.push = function(item){
		for(var i=0; i < this.items.length; i++){
			if(this.items[i]==item) return;
		}
		this.items[this.items.length] = item;
	}
}

function GrupoColumna(tabla,nombre,padre){
	this.nivel = 1;
	this.grupoPadre = (padre||null);
	this.tabla = tabla;
	this.nombre = nombre;
	this.items = new Array();
	this.addColumna=function(ancho, align, nombre, tipoDato){
		var column = new Array();
		column[0] = ancho;
		column[1] = nombre;
		column[2] = (align || 'left');
		if(!tipoDato ||(tipoDato != 'String' && tipoDato != 'Number' && tipoDato != 'Date' && tipoDato != 'Binary'))
			tipoDato = 'String';
		column[3] = tipoDato; //Puede ser String,Number,Date,Binary.
		column[4] = 0; //Orden.
		tabla.columnas[tabla.columnas.length] = column;
		this.items[this.items.length] = column;
	};
	this.addGrupoColumna=function(nombre){
		var grupo =  new GrupoColumna(this.tabla,nombre,this);
		this.items[this.items.length] = grupo;
		this.addNivel();

		return grupo;
	}
	this.addNivel=function(){
		this.nivel++;
		if(this.grupoPadre) this.grupoPadre.addNivel();
	}
	this.numColumnas=function(){
		var numCols=0;
		for(var i=0; i<this.items.length; i++){
			if(this.items[i].nombre) numCols += this.items[i].numColumnas();
			else if(this.items[i][0]>0) numCols+=1;
		}
		return numCols;
	}
	this.toString=function(){
		var str = '';
		var subStr = '<tr>';
		for(var i=0; i<this.items.length; i++){
		  if(!this.items[i].nombre){
		  	if(this.items[i][0]>0){
				var texto = this.items[i][1];
				var idTablaPadre = this.tabla.id;
				if(texto=='') texto = '&nbsp;';
				str += '<td align="center" ';
				if(this.nivel > 1) str += ' rowspan="'+this.nivel+'"';
				str += '><div  id="texto'+i+'" style="overflow:hidden;width:'+((this.items[i][0]*1)-1)+'px; ';
				// Ordenar
				if(this.tabla.ordenar){
					str += 'cursor:'+cursor+';" onclick="ordenaTabla(\''+idTablaPadre+'\','+i+')">';
					str += (this.items[i][4]== 1) ? texto + '&nbsp;<img src="' + TB_ImgPath+'order-up.gif" border=0 align="absbottom">'
						:( (this.items[i][4]== 2)? texto + '&nbsp;<img src="' + TB_ImgPath+'order-down.gif" border=0 align="absbottom">'
						: texto );
				} else {
					str +='">'+this.items[i][1]// lo que habia
				}
				// Fin ordenar
				str += '</div></td>';
			}
		  }else{
		    str += '<td align="center"';
			str += ' colspan="'+this.items[i].numColumnas()+'"';
			str += '>'+this.items[i].nombre+'</td>';
			subStr += this.items[i].toString();
		  }
		}
		if(this.grupoPadre==null){
			//str = '<tr>'+str+'<td width="16px" rowspan="'+this.nivel+'">&nbsp</td>';
		}
		subStr += '</tr>';
		if(subStr=='<tr></tr>') return str;
		else return str+'</tr>'+subStr;
	}
	this.toOrdenInicial=function(){
		for(var i=0; i<this.items.length; i++){
			this.items[i][4]= 0;
		}
	}
}

function Tabla(par,anchoTabla){
  this.id = 'tb_tabla'+(TB_Num++);
  this.parent = par;
  this.parent.tabla = this;

  this.height = '100%';
  this.multiple = false;   //Si se permite seleccionar más de una fila.
  this.ordenar = false;
  this.ordenaColumna = true; //Para que ordene por la columna sobre la que se haga click.
  this.posicionesOriginales = new Array();
  this.ultimoTable = false;
  this.focusedIndex = -1;
  this.selectedIndex = -1;

  this.selectedItems = new Lista();
  this.estructuraColumnas = new GrupoColumna(this);

  this.anchoTabla = (anchoTabla||0);

  this.columnas = new Array();
  this.lineas = new Array();
  // Este array de booleanos se usa para resaltar las filas con observaciones
  this.observaciones = new Array();
  this.originales = new Array();

  this.cabecera = null;
  this.displayCabecera = false;

  this.widthTabla = TB_widthTabla;
  this.addGrupoColumna = TB_addGrupoColumna;
  this.addColumna = TB_addColumna;
  this.addLinea = TB_addLinea;
  this.getLinea = TB_getLinea;
  this.setLinea = TB_setLinea;
  this.removeLinea = TB_removeLinea;
  this.selectLinea = TB_selectLinea;
  this.restoreIndex = TB_restoreIndex;
  this.activaRow = TB_activaRow;
  this.desactivaRow = TB_desactivaRow;
  this.clearTabla = TB_clearTabla;
  this.scroll = TB_scroll;
  this.readOnly = false;
  this.reasignaOrden = TB_reasignaOrden;
  this.asignaOrden = TB_asignaOrden;

  // Se redefine esta funcion (antes no hacia nada) para que tenga en cuenta
  // el array 'observaciones' a la hora de asignar un estilo a la linea
  this.colorLinea = function(i){
    if (this.observaciones.length > i)
      if (this.observaciones[i]) return 'inactivaObs';

    return 'inactiva';
  };

  this.displayTabla = TB_displayTabla;
  this.displayDatos = function(datos){};
  this.getRealSelectedRow = TB_getRealSelectedRow;
  this.setLineas = TB_setLineas;
}

function TB_widthTabla(){
  var anchoTotal = this.anchoTabla;
  if(anchoTotal<=0){
	for(var i=0;i<this.columnas.length;i++){
	  var col = Number(this.columnas[i][0]);
	  if(col!=0) anchoTotal += (2*TB_Padding)+col;
	}
	this.anchoTabla = anchoTotal;
  }
  return anchoTotal;
}

function TB_restoreIndex(){
  if(this.multiple){
	  this.selectedIndex = new Array(this.lineas.length);
	  this.selectedItems = new Lista();
  }else{
	this.selectedIndex = -1;
  }
  this.focusedIndex = -1;
}

function TB_asignaOrden(index){
	var imax = 0;
	for(var i=0; i < this.lineas.length; i++){
		var pos = eval(this.lineas[i][0]);
		if(isNaN(pos)) pos = 0;
		if(pos > imax){
			imax = pos;
		}
	}
	this.lineas[index][0]=imax+1;
	writeInElement(this.id+'_Cell'+index+'_'+0,this.lineas[index][0]);
}

function TB_reasignaOrden(index){
	var ordenIndex = this.lineas[index][0];
	this.lineas[index][0]='';
	writeInElement(this.id+'_Cell'+index+'_'+0,this.lineas[index][0]);
	for(var i=0; i < this.lineas.length; i++){
		var pos = eval(this.lineas[i][0]);
		if(isNaN(pos)) pos = 0;
		if(pos > ordenIndex){
			pos = pos-1;
			this.lineas[i][0]=pos;
			writeInElement(this.id+'_Cell'+i+'_'+0,this.lineas[i][0]);
		}
	}
}

function TB_addGrupoColumna(nombre){
    return this.estructuraColumnas.addGrupoColumna(nombre);
}

function TB_addColumna(ancho, align, nombre,tipoDato){
	this.estructuraColumnas.addColumna(ancho,align,nombre,tipoDato);
}

function TB_addLinea(datos){
  if(datos.length != this.columnas.length) return false;
  tb_addElement(this.lineas,datos);
  this.focusedIndex = -1;
  this.selectedIndex = -1;
  this.displayTabla();
  return true;
}

function TB_getLinea(rowID){
  if (TB_getLinea.arguments.length == 0) rowID = this.selectedIndex;
  if (rowID < 0 || rowID >= this.lineas.length) return false;
  return this.lineas[rowID];
}


function TB_setLinea(datos,rowID){
  if (datos.length != this.columnas.length) return false;
  if (TB_setLinea.arguments.length == 1) rowID = this.selectedIndex;
  if (rowID < 0 || rowID >= this.lineas.length) return false;
  this.lineas[rowID] = datos;
  this.displayTabla();
  return true;
}

function TB_removeLinea(rowID){
  var index = (rowID || this.selectedIndex);
  if (index < 0 || index >= this.lineas.length) return false;
  tb_deleteElement(this.lineas,index);
  this.focusedIndex = -1;
  this.selectedIndex = -1;
  this.displayTabla();
  return true;
}

function TB_clearTabla(){
  this.lineas = new Array();
  this.displayTabla();
  return true;
}

function TB_displayTabla(){
  this.restoreIndex();
  TB_Num += 1;
  var ancho=this.widthTabla();
  var cab = '';
  //CABECERA
  cab += '<div class="xCabeceraDiv" style="width:0px;">';
  cab += '<div id="'+this.id+'DivCab" style="WIDTH:'+(ancho)+'px;overflow:hidden;">';
  if(this.cabecera==null){
  	cab += '<table class="xCabecera" rules=all cellspacing=0 cellpadding='+TB_Padding+'>';
	cab += this.estructuraColumnas.toString();
  	cab += '</table>';
  }else{
  	cab += this.cabecera;
  }
  cab += '</div>';
  cab += '</div>';
  //FIN CABECERA
  str = '<div class="xCabeceraDiv" style="width:'+(ancho)+'px;">';
  str += '<div  id="'+this.id+'DivTabla" style="WIDTH:'+(ancho)+'px; HEIGHT:'+this.height+'; overflow-x:hidden;overflow-y:hidden;" onscroll="scrollCabecera(\''+this.id+'\')">';
  str += '<table id="'+this.id+'" name="'+this.id+'" class="xTabla" cellspacing=0 cellpadding='+TB_Padding+' onselectstart="return false;" onclick="tb_selectRow(event);" ondblclick="tb_callFromTableTo(event);">';
  var ultimaColumna=-1;
  for(var i=0; i < this.columnas.length; i++){
   	if(this.columnas[i][0]>0){
		str += '<col id="'+this.id+'_col_'+i+'" style="text-align:'+this.columnas[i][2]+';width:'+((2*TB_Padding)+(this.columnas[i][0]*1))+';">';
		ultimaColumna = i;
	}
  }
  for(var i=0; i < this.lineas.length; i++){
    // La funcion this.colorLinea se declara en el constructor Tabla() pero
    // OJO en algunas jsp se redefine, por ej. listadoAsientosHistorico.jsp
    str += '<tr class="'+this.colorLinea(i)+'">';
    var lin = this.lineas[i];
    for(var j=0; j < lin.length; j++){	  
      if(this.columnas[j][0]>0) str += '<td align=\"' + this.columnas[j][2] + '\" width="'+this.columnas[j][0]+'">'+(lin[j]==''?'&nbsp;':lin[j])+'</td>';
    }
    str += '</tr>';
  }
  str += '</table>';
  str += '</div>';
  str += '</div>';
  var div = document.createElement("DIV");
  div.tabla = this;
  div.innerHTML = ((this.displayCabecera)?cab:'')+str;
  div.style.width = ancho;
  var oChild = null;

/** original **/
//var oChild=this.parent.children(0);

 try{
    if(this.parent){
        var hijos = new Array();
        hijos = this.parent.children;
        if(hijos!=null && hijos.length>0)
            oChild = hijos[0];            
    }//if
  }catch(Err){
      alert(Err);
  }

  if(oChild){ this.parent.removeChild(oChild); }
  this.parent.appendChild(div);
  if(ultimaColumna >= 0){
	var tab=document.getElementById(this.id);
	var d = document.getElementById(this.id+'_col_'+ultimaColumna);
	if(tab.scrollHeight > this.height) d.style.width=((((2*TB_Padding)+(this.columnas[ultimaColumna][0]*1))-16)+'px');
  }
}

function scrollCabecera(tabla){
  if(document.getElementById(tabla+'DivCab') && document.getElementById(tabla+'DivTabla'))
  	document.getElementById(tabla+'DivCab').scrollLeft = document.getElementById(tabla+'DivTabla').scrollLeft;
}

function TB_selectLinea(rowID){
  if(this.readOnly) return;
  if(!this.multiple && this.selectedIndex >= 0) {
    this.desactivaRow(this.selectedIndex);
  }
  var isSelected = false;
  if(this.multiple){
    isSelected = (this.selectedIndex[rowID]);
  }else{
 	isSelected = (this.selectedIndex == rowID);
  }

  if(isSelected) {
	this.desactivaRow(rowID);
	if(this.multiple) {
		this.selectedIndex[rowID] = false;
		this.selectedItems.pop(rowID);
	} else{
		this.selectedIndex = -1;
		this.focusedIndex = -1;
		this.scroll();
	}
	var datos = new Array();
	for (i=0; i<this.columnas.length;i++){
      datos[i] = '';
	}
	this.displayDatos(datos);
  }else{
	this.activaRow(rowID);

	if(this.multiple){
	    this.selectedIndex[rowID] = true;
		this.selectedItems.push(rowID);
	} else{
		this.selectedIndex = rowID;
		this.focusedIndex = rowID;
		this.scroll();
	}

	this.displayDatos(this.lineas[rowID]);
  }
}

function TB_activaRow(rowID){
    if(this.readOnly) return;
	var table = document.getElementById(this.id);
	if(rowID < 0 || rowID>=table.rows.length) return;
    var selRow = table.rows[rowID*1];
	selRow.className = 'activa';
}

function TB_desactivaRow(rowID){
    if(this.readOnly) return;
	var table = document.getElementById(this.id);
	if(rowID < 0 || rowID>=table.rows.length) return;
	//var selRow = table.rows(rowID*1);
    var selRow = table.rows[rowID*1];
	selRow.className = this.colorLinea(rowID);
}

function TB_scroll(){
	if(this.focusedIndex < 0 || this.focusedIndex >= this.lineas.length) return;
    var selRow = document.getElementById(this.id).rows[this.focusedIndex*1];
	//var selRow = document.getElementById(this.id).rows(this.focusedIndex*1);
  	var selDiv = document.getElementById(this.id+'DivTabla');
  	if(selRow.offsetTop < selDiv.scrollTop)
    	selDiv.scrollTop = selRow.offsetTop;
  	else if(selRow.offsetTop > (selDiv.scrollTop+selDiv.clientHeight-selRow.offsetHeight))
      	selDiv.scrollTop = (selRow.offsetTop-selDiv.clientHeight+selRow.offsetHeight);

}


function selectRow(rowID,tableName){
  if (document.all){
    with(eval('document.all.'+tableName+'.parentElement.parentElement.parentElement')){
      tabla.selectLinea(rowID);
    }
  }else if (document.getElementById){
    with(document.getElementById(tableName).parentNode.parentNode.parentNode){
      tabla.selectLinea(rowID);
    }
  }

  callClick(rowID,tableName);
  return false;
}


function tb_selectRow(event){
 if(window.event)  {
  if(window.event.srcElement.tagName!='TD') return false;
  rowID = window.event.srcElement.parentElement.rowIndex;
  tableName = window.event.srcElement.parentElement.parentElement.parentElement.id;
  return selectRow(rowID,tableName);
  }else{
      if(event.target.tagName!='TD') return false;
      rowID = event.target.parentNode.rowIndex;
      tableName = event.target.parentNode.parentNode.parentNode.id;
      return selectRow(rowID,tableName);
  }
}


function tb_callFromTableTo(event){

    if(window.event) { //IE
  if(window.event.srcElement.tagName!='TD') return false;
  rowID = window.event.srcElement.parentElement.rowIndex;
  tableName = window.event.srcElement.parentElement.parentElement.parentElement.id;
  return callFromTableTo(rowID,tableName);
    }else{ // FF
        if(event.target.tagName!='TD') return false;
        rowID = event.target.parentNode.rowIndex;
        tableName = event.target.parentNode.parentNode.parentNode.id;
        return callFromTableTo(rowID,tableName);
}
}


function TB_getRealSelectedRow(rowID){
  if (this.multiple) {
	if (TB_getRealSelectedRow.arguments.length == 0) {
		var posiciones = new Array();
		var seleccionados = this.selectedIndex;
		for (var i=0; i<seleccionados.length; i++){
			if (seleccionados[i]==true) {
				if (this.posicionesOriginales.length==0)
					posiciones[posiciones.length]= i;
				else
					posiciones[posiciones.length]= this.posicionesOriginales[i];
			}
		}
		return posiciones;
	} else {
		if (this.posicionesOriginales.length==0)
			return rowID;
		else
			return this.posicionesOriginales[rowID];
	}
  } else {
  	if (TB_getRealSelectedRow.arguments.length == 0) rowID = this.selectedIndex;
  	if (rowID < 0 || rowID >= this.lineas.length) return -1;
	if (this.posicionesOriginales.length==0)
		return rowID;
	else
		return this.posicionesOriginales[rowID];
  }
}

function TB_setLineas(datos){
	this.lineas = new Array();
	for (var k=0; k < datos.length; k++) {
			this.lineas[k] = new Array();
			for (var m=0; m<datos[k].length; m++){
				this.lineas[k][m]=datos[k][m];
			}
	}
	this.estructuraColumnas.toOrdenInicial();
	this.originales = new Array();
	for (var k=0; k < this.lineas.length; k++) {
		this.originales[k] = new Array();
		for (var m=0; m<this.lineas[k].length; m++){
				this.originales[k][m]=this.lineas[k][m];
		}
	}
	this.posicionesOriginales = new Array();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//																									  //
//			      Funciones que gestionan los eventos del teclado y del ratón						  //
//																									  //
////////////////////////////////////////////////////////////////////////////////////////////////////////

function pushEnterTable(tableName,listName){
	if(tableName.multiple){
		if((tableName.focusedIndex>-1)&&(tableName.focusedIndex < listName.length)&&(!tableName.ultimoTable)){
			tableName.selectLinea(tableName.focusedIndex);
			desactiveFocusTable(tableName, tableName.focusedIndex);

			tableName.focusedIndex++;
			if(tableName.focusedIndex == listName.length) {
				tableName.focusedIndex--;
				tableName.ultimoTable = true;
			}

			activeFocusTable(tableName, tableName.focusedIndex);
			tableName.scroll();
			rellenarDatosMultiple(tableName, tableName.focusedIndex); //No es general.
		}
	}else{
		callFromTableTo(tableName.focusedIndex, tableName.id); //No es general.
	}
}

function upDownTable(tableName,listName,tecla){
   	var selRow = "";
	if(tableName.multiple){
		desactiveFocusTable(tableName, tableName.focusedIndex);
	}else{
		if(tableName.focusedIndex!=-1) tableName.desactivaRow(tableName.focusedIndex);
	}

	if(tecla == 40){
		tableName.focusedIndex++;
		if(!tableName.multiple) tableName.selectedIndex = tableName.focusedIndex;
		if(tableName.focusedIndex == listName.length) {
			tableName.focusedIndex--;
			if(!tableName.multiple) tableName.selectedIndex = tableName.focusedIndex;
			tableName.ultimoTable = true;
		}
	}
	if(tecla == 38){
		if(!tableName.ultimoTable){
			if(tableName.focusedIndex > -1 ){
				tableName.focusedIndex--;
				if(!tableName.multiple) tableName.selectedIndex = tableName.focusedIndex;
			}
		}else{
			tableName.ultimoTable = false;
		}
	}

	if(tableName.multiple){
		rellenarDatosMultipleFocus(tableName, tableName.focusedIndex) //No es general.
		if(!tableName.ultimoTable){
			activeFocusTable(tableName, tableName.focusedIndex);
			tableName.scroll();
		}
	}else{
		rellenarDatos(tableName, tableName.focusedIndex); //No es general.
		if(!tableName.ultimoTable){
			if(tableName.focusedIndex!=-1) tableName.activaRow(tableName.focusedIndex);
			tableName.scroll();
		}
	}
}

function callClick(rowID,idTableName){
	window.focus(); //Evita el scroll del DIV para scrollControlTable.

    if(document.all) // IE
	var tableObject = eval('document.all.'+idTableName+'.parentElement.parentElement.parentElement.tabla');
    else // FF
        var tableObject = eval('document.getElementById(\"'+idTableName+'\").parentNode.parentNode.parentNode.tabla');

	tableObject.ultimoTable = false;
	if(tableObject.multiple){
		desactiveFocusTable(tableObject, tableObject.focusedIndex);
		if(!event.ctrlKey) desactiveSelectedRows(tableObject, rowID);
		if(event.shiftKey) activeSelectedRows(tableObject, rowID);
		tableObject.focusedIndex = rowID;
		this.scroll();
		activeFocusTable(tableObject, rowID);
		rellenarDatosMultipleFocus(tableObject, rowID) //No es general.
		rellenarDatosMultiple(tableObject, rowID);// No es general
	}else{
		rellenarDatos(tableObject, rowID); //No es general.
	}
}

function activeFocusTable(tableName, rowID){
	if((!tableName.ultimoTable)&&(rowID > -1)){
		var selRow = document.getElementById(tableName.id).rows(rowID*1);
		var selRowUp = (rowID>0)?document.getElementById(tableName.id).rows(rowID-1):null;
		var numCol = selRow.cells.length;
		if(numCol == 1) {
			selRow.cells(0).className = 'xTablaCellOne';
			if(selRowUp) selRowUp.cells(0).className='xTablaCellUp';
		} else if(numCol > 1){
			selRow.cells(0).className = 'xTablaCellFirst';
			selRow.cells(numCol-1).className = 'xTablaCellLast';
			if(selRowUp){
				selRowUp.cells(0).className='xTablaCellUp';
				for(var i=1; i < (numCol-1); i++){
					selRow.cells(i).className = 'xTablaCell';
					selRowUp.cells(i).className='xTablaCellUp';
				}
				selRowUp.cells(numCol-1).className='xTablaCellUp';
			}else{
				for(var i=1; i < (numCol-1); i++){
					selRow.cells(i).className = 'xTablaCell';
				}
			}
		}
	}
}

function desactiveFocusTable(tableName, rowID){
	if(rowID > -1){
		var selRow = document.getElementById(tableName.id).rows(rowID*1);
		var selRowUp = (rowID>0)?document.getElementById(tableName.id).rows(rowID-1):null;
		for(var i=0; i<selRow.cells.length; i++){
			selRow.cells(i).className = '';
			if(selRowUp) selRowUp.cells(i).className = '';
		}
	}
}

function activeSelectedRows(tableObject, rowID){
	var inicio = tableObject.focusedIndex;
	var fin = rowID;
	if(rowID < tableObject.focusedIndex){
		inicio = rowID;
		fin = tableObject.focusedIndex;
	}
	var items = tableObject.selectedItems.items;
	for(var i=0; i < items.length; i++){
		tableObject.desactivaRow(items[i]);
	}
	tableObject.selectedIndex = new Array(tableObject.lineas.length);
	tableObject.selectedItems = new Lista();

	for(var i=inicio;i<=fin;i++){
		tableObject.activaRow(i);
		tableObject.selectedIndex[i] = true;
		tableObject.selectedItems.push(i);
	}
}

function desactiveSelectedRows(tableObject, rowID){
	var items = tableObject.selectedItems.items;
	for(var i=0; i < items.length; i++){
		if(items[i]!=rowID){
			tableObject.desactivaRow(items[i]);
			tableObject.selectedIndex[items[i]] = false;
			tableObject.selectedItems.pop(items[i]);
		}
	}
}

// Ordenar columnas

var column_to_sort = -1;

function ordenaTabla(idTableName,columnID){

	//var tableObject = table;
	//var tableObject = document.getElementById(idTableName);
	var tableObject = eval('document.all.'+idTableName+'.parentElement.parentElement.parentElement.tabla');

	if(!tableObject.ordenaColumna) return;
	column_to_sort = columnID;

	for (var i=0; i < tableObject.estructuraColumnas.items.length; i++){
		if(i!=columnID) tableObject.estructuraColumnas.items[i][4] = 0;
	}

	if(tableObject.originales.length==0) {
		for (var k=0; k < tableObject.lineas.length; k++) {
			tableObject.originales[k] = new Array();
			for (var m=0; m<tableObject.lineas[k].length; m++){
				tableObject.originales[k][m]=tableObject.lineas[k][m];
			}
		}
	}

	tableObject.estructuraColumnas.items[columnID][4] = ((tableObject.estructuraColumnas.items[columnID][4]+1)%3);

	//tableObject.lineas = tableObject.originales;

	/* Mi apaño */
	for (var k=0; k < tableObject.originales.length; k++) {
			tableObject.lineas[k] = new Array();
			for (var m=0; m<tableObject.originales[k].length; m++){
				tableObject.lineas[k][m]=tableObject.originales[k][m];
			}
			var g = tableObject.lineas[k].length;
			tableObject.lineas[k][g]=k;
	}

	/* Fin mi apaño */

	if(tableObject.estructuraColumnas.items[columnID][4]==1){
		if(tableObject.estructuraColumnas.items[columnID][3]== 'Number')
			tableObject.lineas.sort(tb_compararNumberAsc);
		else if(tableObject.estructuraColumnas.items[columnID][3]== 'Date')
			tableObject.lineas.sort(tb_compararDateAsc);
		else
			tableObject.lineas.sort(tb_compararStringAsc);
	}else if(tableObject.estructuraColumnas.items[columnID][4]==2){
		if(tableObject.estructuraColumnas.items[columnID][3]== 'Number')
			tableObject.lineas.sort(tb_compararNumberDesc);
		else if(tableObject.estructuraColumnas.items[columnID][3]== 'Date')
			tableObject.lineas.sort(tb_compararDateDesc);
		else
			tableObject.lineas.sort(tb_compararStringDesc);
	}
	/* Mi apaño */
	var lineasOriginales = new Array();
	var posOrig = new Array();
	tableObject.posicionesOriginales = new Array();

	for (var k=0; k < tableObject.lineas.length; k++) {
		lineasOriginales[k] = new Array();
		for (var m=0; m<eval(tableObject.lineas[k].length-1); m++){
			lineasOriginales[k][m]=tableObject.lineas[k][m];
		}
		posOrig[k]=tableObject.lineas[k][eval(tableObject.lineas[k].length-1)];
		tableObject.lineas[k]=new Array();
		tableObject.lineas[k]=lineasOriginales[k];
	}
	tableObject.lineas = lineasOriginales;
	tableObject.posicionesOriginales = posOrig;
	tableObject.restoreIndex();
	if(tableObject.multiple){
	} else {
		rellenarDatos(tableObject, tableObject.focusedIndex);
	}
	/* Fin mi apaño */
	tableObject.displayTabla();
}

function tb_compararStringAsc(e1,e2){
	return e1[column_to_sort] < e2[column_to_sort] ? -1 : e1[column_to_sort] == e2[column_to_sort] ? 0 : 1;
}
function tb_compararStringDesc(e1,e2){
	return e2[column_to_sort] < e1[column_to_sort] ? -1 : e2[column_to_sort] == e1[column_to_sort] ? 0 : 1;
}
function tb_compararNumberAsc(e1,e2){
	var num1,num2;
	num1 = tb_parseNumber(e1[column_to_sort]);
	num2 = tb_parseNumber(e2[column_to_sort]);

	return num1 < num2 ? -1 : num1 == num2 ? 0 : 1;
}
function tb_compararNumberDesc(e1,e2){
	var num1,num2;
	num1 = tb_parseNumber(e1[column_to_sort]);
	num2 = tb_parseNumber(e2[column_to_sort]);

	return num2 < num1 ? -1 : num2 == num1 ? 0 : 1;
}
function tb_compararDateAsc(e1,e2){
	var num1,num2;
	num1 = tb_parseDate(e1[column_to_sort]);
	num2 = tb_parseDate(e2[column_to_sort]);
	return num1== null? -1 : num2 == null? 1 : num1 < num2 ? -1 : num1 == num2 ? 0 : 1;
}
function tb_compararDateDesc(e1,e2){
	var num1,num2;
	num1 = tb_parseDate(e1[column_to_sort]);
	num2 = tb_parseDate(e2[column_to_sort]);

	return num2== null? -1 : num1 == null? 1 : num2 < num1 ? -1 : num2 == num1 ? 0 : 1;
}

function tb_parseDate(fecha){
	var sArray = new Array();
	sArray = fecha.split("/");
    if ((sArray.length < 3) || (sArray.length > 3))
    	sArray = fecha.split("-");
    if ((sArray.length < 3) || (sArray.length > 3))
        return null;

    day = sArray[0];
    month = sArray[1];
    year = sArray[2];

	if(isNaN(day) || isNaN(month) || isNaN(year)) return null;
	else return new Date(year,(month-1),day);
}

function tb_parseNumber(num){
	var valor = num;
	//Si el parametro no es válido, devolvemos 0.
	if(valor==null || valor=='undefined' || valor=='') return null;

	var numString = ''+valor;
	//Eliminamos todos los caracters del campo salvo los dígitos y la coma decimal.
	var reg = /[^0-9\,]/g;
	numString = numString.replace(reg,'');
	//Cambiamos la coma decimal por el punto.
	var sep = /,/gi;
	numString = numString.replace(sep, '.');

	//Si despues de quitar los puntos y convertir la coma a punto
	// sigue sin ser un número devolvemos 0.
	if(isNaN(numString)) return null;
	else return (numString*1);
}

// Funciones a redefinir en la jsp.

function callFromTableTo(rowID,tableName){}	//Se ejecuta en el evento onDbClick.

function rellenarDatos(tableName,rowID){}	//Se ejecuta al cambiar la selección en una Tabla Simple.
function rellenarDatosMultiple(tableName,rowID){}	//Se ejecuta al cambiar la selección en una Tabla Múltiple.
													//Se actuará en función de la selección (tableName.selectedIndex[rowID] a true o false) de rowID.
													//Se estudiarán los casos de rowID a -1 o tableName.ultimoTable a false.
function rellenarDatosMultipleFocus(tableName,rowID){}	//Se ejecuta al cambiar el foco en una Tabla Múltiple.
														//Se estudiarán los casos de rowID a -1 o tableName.ultimoTable a false.

////////////////////////////////////////////////////////////////////////////////////////////////////////