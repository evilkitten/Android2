Global TEMP_DIR$

Const PUBLISHER$="EKD"
Const APP_TITLE$="Android 2"

Const MESH_EXTENSION$=".3ds"

Const ANIM_EXTENSION$="_Anim"
Const MAT_EXTENSION$="_Mat"

Function Initialise()
	SPECTRUM_MODE=Instr(CommandLine(),"/zx")
	InitialiseAssets
	RUNTIME_Example
End Function

Function AcquireTextureMap(Filepath$)
	Return LoadTexture(Filepath,257)
End Function
		
Function MatFile$(Value$)
	Return VisualDir()+"Assets\"+Value+MAT_EXTENSION+IMG_EXTENSION
	;Return TempDir()+Value+MAT_EXTENSION+IMG_EXTENSION
End Function

Function AnimFile$(Value$)
	Return VisualDir()+"Assets\"+Value+ANIM_EXTENSION+MESH_EXTENSION
	;Return TempDir()+Value+ANIM_EXTENSION+MESH_EXTENSION
End Function

Function InitialiseTempDir()
	TEMP_DIR=GetEnv("TMP")+"\"+PUBLISHER+"\"
	
	If (FileType(TEMP_DIR)<>2)
		CreateDir TEMP_DIR
	End If
	
	If (FileType(TEMP_DIR)<>2)
		RuntimeError(TEMP_DIR+" could not be created.")
	End If
	
	TEMP_DIR$=TEMP_DIR$+APP_TITLE$+"\"
	
	If (FileType(TEMP_DIR)<>2)
		CreateDir TEMP_DIR
	End If
	
	If (FileType(TEMP_DIR)<>2)
		RuntimeError(TEMP_DIR+" could not be created.")
	End If
End Function

Function TempDir$()
	Return TEMP_DIR$
End Function

Function InitialiseAssets()
	InitialiseSharedFilesystem
	InitialiseTempDir
	UnPackAssets
End Function

;~IDEal Editor Parameters:
;~F#10#14#19#1E#34
;~C#Blitz3D