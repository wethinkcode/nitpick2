package za.co.wethinkcode.core

import java.nio.file.Path

@JvmField val DSL_LEAF: Path = Path.of("pick.dsl")
@JvmField val LMS_FOLDER: Path = Path.of(".lms")
@JvmField val LONG_RESULT_LEAF: Path = Path.of("nitpick.txt")

@JvmField val GRADE_RESULT_LEAF: Path = Path.of("grade.txt")

@JvmField val PROTECTED_TXT_LEAF: Path = Path.of("protected.txt")

@JvmField val META_TXT_LEAF: Path = Path.of("meta.txt")

@JvmField val VAULT_FOLDER: Path = Path.of(".vault")

public const val DSL_COMMENT: String = "#"
public const val DSL_BASH: String = "bash "
