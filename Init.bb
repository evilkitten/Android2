Global ROOT$=CurrentDir();SystemProperty("appdir")

Const VISUAL_DIR_NAME$="Visual"
Const ANIM_EXTENSION$="_Anim.3ds"
Const MAT_EXTENSION$="_Mat.png"

Function MatFile$(Value$)
	Return VisualDir()+Value+MAT_EXTENSION
End Function

Function AnimFile$(Value$)
	Return VisualDir()+Value+anim_EXTENSION
End Function

Function VisualDir$()
	Return ROOT+VISUAL_DIR_NAME$+"\"
End Function

;~IDEal Editor Parameters:
;~F#6#A#E
;~C#Blitz3D