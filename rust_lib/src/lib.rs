mod java_glue;

pub use crate::java_glue::*;

use android_logger::Config;
use log::LevelFilter;
use rifgen::rifgen_attr::*;

pub struct RustLog;

impl RustLog {
    //set up logging
    #[generate_interface]
    pub fn initialise_logging() {
        #[cfg(target_os = "android")]
            android_logger::init_once(
            Config::default()
                .with_max_level(LevelFilter::Trace)
                .with_tag("Rust"),
        );
        log_panics::init();
        log::info!("Logging initialised from Rust");
    }
}

pub struct Exercise {
    name: String,
    weight: i64,
    reps: i64,
    sets: i64
}

impl Exercise {
    #[generate_interface(constructor)]
    pub fn new(name: String,
               weight: i64,
               reps: i64,
               sets: i64) -> Exercise {
        Self {
            name,
            weight,
            reps,
            sets
        }
    }
    #[generate_interface]
    pub fn to_string(&self) -> String {
        return format!("{} with {} sets of {} reps of {} lbs ",self.name, self.sets, self.reps, self.weight);
    }

}

