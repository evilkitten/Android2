Const CTRL_HORIZONTAL=0
Const CTRL_VERTICAL=1
Const CTRL_FIRE=2

Const CTRL_MAX=2

Const KEY_DEF_LEFT=203
Const KEY_DEF_RIGHT=205
Const KEY_DEF_UP=200
Const KEY_DEF_DOWN=208
Const KEY_DEF_FIRE=57
Const JOY_DEF_FIRE=1
Const JOY_DEF_PORT=0
Const JOY_DEF_X_DEADZONE#=0.25
Const JOY_DEF_Y_DEADZONE#=0.25
Const JOY_DEF_X_REVERSED=False
Const JOY_DEF_Y_REVERSED=True

Const JOY_ALTERNATIVE_0=0;;(Default, X and Y Axis)
Const JOY_ALTERNATIVE_1=1;;(Use Z Axis instead of Y)
Const JOY_ALTERNATIVE_2=2;;(Use Y Axis instead of X and X instead of Y)
Const JOY_ALTERNATIVE_3=4;;(Use Z Axis instead of X and X instead of Y)

Const JOY_DEF_ALTERNATIVE=JOY_ALTERNATIVE_0;Default, X and Y Axis

Global KEY_LEFT=KEY_DEF_LEFT
Global KEY_RIGHT=KEY_DEF_RIGHT
Global KEY_UP=KEY_DEF_UP
Global KEY_DOWN=KEY_DEF_DOWN
Global KEY_FIRE=KEY_DEF_FIRE

Global JOY_FIRE=JOY_DEF_FIRE
Global JOY_PORT=JOY_DEF_PORT
Global JOY_X_DEADZONE#=JOY_DEF_X_DEADZONE#
Global JOY_Y_DEADZONE#=JOY_DEF_Y_DEADZONE#
Global JOY_X_REVERSED=JOY_DEF_X_REVERSED
Global JOY_Y_REVERSED=JOY_DEF_Y_REVERSED
Global JOY_ALTERNATIVE=JOY_DEF_ALTERNATIVE

Function GetControlInput(Control[2])
	;0=X
	;1=Y
	;2=FIRE
	
	Control[CTRL_HORIZONTAL]=Sgn(KeyboardInputX()+JoypadInputX())
	Control[CTRL_VERTICAL]=Sgn(KeyboardInputY()+JoypadInputY())
	Control[CTRL_FIRE]=Sgn(KeyboardInputFire()+JoypadInputFire())
End Function

Function KeyboardInputX()
	Return KeyDown(KEY_RIGHT)-KeyDown(KEY_LEFT)
End Function

Function JoypadInputX()
	Local X
	Local XX
	
	Select JOY_ALTERNATIVE
		Case JOY_ALTERNATIVE_1:	
			XX=JoyX(JOY_PORT) 
		Case JOY_ALTERNATIVE_2:	
			XX=JoyY(JOY_PORT) 
		Case JOY_ALTERNATIVE_3:	
			XX=JoyZ(JOY_PORT) 
		Default:
			;JOY_ALTERNATIVE_0:
			XX=JoyX(JOY_PORT) 
	End Select
	X=Sgn(XX)*(Abs(XX)>JOY_X_DEADZONE)
	If (JOY_X_REVERSED) Then X=0-X
	Return X
End Function

Function KeyboardInputY()
	Return KeyDown(KEY_UP)-KeyDown(KEY_DOWN)
End Function

Function JoypadInputY()
	Local Y
	Local YY
	
	Select JOY_ALTERNATIVE
		Case JOY_ALTERNATIVE_1:	
			YY=JoyZ(JOY_PORT) 
		Case JOY_ALTERNATIVE_2:	
			YY=JoyX(JOY_PORT) 
		Case JOY_ALTERNATIVE_3:	
			YY=JoyX(JOY_PORT) 
		Default:
			;JOY_ALTERNATIVE_0:
			YY=JoyY(JOY_PORT) 
	End Select
	Y=Sgn(YY)*(Abs(YY)>JOY_Y_DEADZONE)
	If (JOY_Y_REVERSED) Then Y=0-Y
	Return Y
End Function

Function KeyboardInputFire()
	Return KeyDown(KEY_FIRE)
End Function

Function JoypadInputFire()
	Local Y=JoyDown(JOY_FIRE,JOY_PORT)
	Return Y
End Function
;~IDEal Editor Parameters:
;~F#27#31#35#49#4D#61#65
;~C#Blitz3D