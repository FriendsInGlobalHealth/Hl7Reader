<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<c:url var="search" value="/pesquisarHl7"/> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Resultado da Pesquisa</title>
<style type="text/css">
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
<script>
function validateForm() {
  var x = document.forms["myForm"]["partialNid"].value;
  if (x == "") {
    alert("O NID Parcial deve ser preenchido");
    return false;
  }
}
</script>
</head>
<body>
	
	<h2>PESQUISA DO PACIENTE NO FICHEIRO HL7</h2>
	
	<form name="myForm" action="${search}" onsubmit="return validateForm()" method="get">
	
		Nid Parcial (exemplo: /2021/00001)<input type="text" name="partialNid">   
		
		<input type="submit" value="Pesquisar">  
	
	</form>

	<c:if test="${not empty filteredPatients}">
		<h2>Resultado da pesquisa:</h2>
			
		<table>
			<thead>
				<tr>
					<th>NID</th>
					<th>Nome</th>
					<th>Sobrenome</th>
					<th>Apelido</th>
					<th>Data de Nascimento</th>
					<th>Sexo</th>
					<th>Endereço</th>
				</tr>
			</thead>
		<tbody>
		    <c:forEach items="${filteredPatients}" var="patient">
			    <tr>
			        <td>${patient.nid}</td>
			        <td>${patient.firstName}</td>
			        <td>${patient.middleName}</td>
			        <td>${patient.lastName}</td>
			        <td>${patient.dateOfBirth}</td>
			        <td>${patient.gender}</td>
			        <td>${patient.address}</td>
			    </tr>
		    </c:forEach>
		</tbody>                        
		</table >
	</c:if>
</body>
</html>