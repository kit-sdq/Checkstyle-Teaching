package edu.kit.informatik;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

enum TaskA implements TestCase {
	PUBLIC {
		@Override
		public void process(final Consumer<? super String> consumer) {

		}
	}
}
