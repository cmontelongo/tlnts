<!DOCTYPE html><html class=''>
<head>
	<title>Inicio - Entrar</title>
	<style>/* Design based on Blue Login Field of Kevin Sleger https://codepen.io/MurmeltierS/pen/macKb */
		body {
			background: #BDBDBD;
			background-size: cover;
			font-family: "Trebuchet MS", "Roboto", helvetica, arial, sans-serif;
			-webkit-font-smoothing: antialiased;
			-moz-osx-font-smoothing: grayscale;
		}
		body::before {
		  z-index: -1;
		  content: '';
		  position: fixed;
		  top: 0;
		  left: 0;
		  background: #BDBDBD;
		  /* IE Fallback */
		  background: #BDBDBD;
		  width: 100%;
		  height: 100%;
		}
		.form {
		  position: absolute;
		  top: 50%;
		  left: 50%;
		  background: #fff;
		  width: 285px;
		  margin: -140px 0 0 -182px;
		  padding: 40px;
		  box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
		}
		.form h2 {
		  margin: 0 0 20px;
		  line-height: 1;
		  color: black;
		  font-size: 30px;
		  font-weight: 400;
		}
		.form input {
		  outline: none;
		  display: block;
		  width: 100%;
		  margin: 0 0 20px;
		  padding: 10px 15px;
		  border: 1px solid #ccc;
		  color: #ccc;
		  box-sizing: border-box;
		  font-size: 25px;
		  font-wieght: 400;
		  -webkit-font-smoothing: antialiased;
		  -moz-osx-font-smoothing: grayscale;
		  transition: 0.2s linear;
		}
		.form inputinput:focus {
		  color: #333;
		  border: 1px solid #44c4e7;
		}
		.form button {
		  cursor: pointer;
		  background: #BDBDBD;
		  width: 100%;
		  padding: 10px 15px;
		  border: 0;
		  color: black;
		  font-size: 50px;
		  font-weight: 600;
		}
		.form button:hover {
		  background: black;
		  color: white;
		}
		.error,
		.valid {
		  display: none;
		  font-size: 15px;
		  font-weight: 600;
		}
		.error {
		  color: red;
		}
	</style>
	</head>
<body>
<!--Correct username: invitado / pass: -->

<section class="form animated flipInX">
  <h2>Entrar</h2>
  <p class="valid">Valido. Favor de esperar un momento.</p>
  <p class="error">Error. Favor de proporcionar los datos de su cuenta para continuar.</p>
  <form class="loginbox" autocomplete="off">
    <input placeholder="Usuario" type="text" id="username"></input>
    <input placeholder="Contraseña" type="password" id="password"></input>
<button id="submit">Entrar</button>
</form>
</section>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script >
$(document).ready(function() {
    $('#username').focus();

    $('#submit').click(function(event) {

        event.preventDefault(); // prevent PageReLoad

		var ValidEmail = $('#username').val() === 'invitado'; // User validate
		var ValidPassword = $('#password').val() === ''; // Password validate

        if (ValidEmail === true && ValidPassword === true) { // if ValidEmail & ValidPassword
            $('.error').css('display', 'none'); // show error msg
            $('.valid').css('display', 'block');
            window.location = "./index.jsp"; // go to home.html
        }
        else {
            $('.error').css('display', 'block'); // show error msg
        }
    });
});
</script>
</body></html>